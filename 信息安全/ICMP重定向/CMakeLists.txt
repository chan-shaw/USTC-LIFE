cmake_minimum_required(VERSION 3.13)
project(ICMP C)

set(CMAKE_C_STANDARD 99)

include(FindPCAP.cmake)

add_executable(ICMP main.c)
target_link_libraries(ICMP ${PCAP_LIBRARY})