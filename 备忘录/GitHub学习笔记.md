# GitHub学习笔记

## GitHub学习笔记一 大纲

### 什么是GitHub

- 一个协作平台
- 一个功能强大的版本控制器
- 可以帮助人们做更多的事

### Exploring a GitHub repository(探索github存储库)

在`github repository`中我们可以找到

- project board:项目板，在github中创建任务
- wiki：创建和存储相关的项目文档
- Insights：查看下拉菜单，包括指向存储库的分析工具的链接。包括
  - pulse：查找有关已完成工作的信息以及此项目仪表板中正在进行的工作
  - Graphs：图表提供了更详细的存储库活动视图，包括谁为存储库做出了贡献，谁将其分叉，以及何时完成工作
- 特殊文档，包括：
  - `README.md`
  - `CONTRIBUTING.md`：`CONTRIBUTING.md`用于描述为存储库做贡献的过程。只要`CONTRIBUTING.md`有人创建新问题或拉取请求，就会显示该文件的链接。
  - `ISSUE_TEMPLATE.md`：`ISSUE_TEMPLATE.md`是可以用来预先填充问题正文的另一个文件。例如，如果您始终需要相同类型的错误报告信息，请将其包含在问题模板中，并且每个新问题都将使用您推荐的启动文本打开。

### Using issues

- issues:针对代码中的bug,代码审查，或者仅仅是其他的一些事进行讨论

- Issue titles：像一个邮件头，告诉小伙伴哪有问题

### Managing notifications 管理通知

一旦对问题或者请求发表了评论，当线程中有活动的时候，就会接收电子邮件通知。



#### 第一步 Assign yourself

未分配的问题没有人在意。当你被分配到问题或者一个拉去请求的时候，它会告诉存储库访问者和贡献者，你将促进对话或者任务

- 在屏幕右侧的“Assignees”部分下，单击齿轮图标并选择您自己

#### 第二步 Open github page

- 单击此存储库中的setting选项卡
- 向下滚动到“GitHub Pages”部分
- 从“Source”下拉列表中选择**master branch**

#### 第三步 close issues

- Click the **Close issue** button below



### Introduction to GitHub flow  github流介绍

分支是GitHub流程的重要组成部分，因为它们允许我们将我们的工作与`master`分支分开。换句话说，在您做出贡献时，每个人的工作都是安全的。

#### 使用分支的提示

单个项目可以有数百个分支，每个分支都表明对分支进行了新的更改`master`。

保持分支机构与团队组织的最佳方法是保持简洁和短暂。换句话说，单个分支应代表单个新功能或错误修复。当分支在合并之前仅活动几天时，这可以减少贡献者之间的混淆[📖](https://help.github.com/articles/github-glossary/#merge)进入`master`分行。

#### 第四步  Create a branch 创建分支

- Navigate to the Code tab
- Click Branch: master in the drop-down
- In the field, enter a name for your branch
- Click Create branch:  or press the “Enter” key to create your branch

#### 第五步 Commit a file 提交文件

创建分支允许我们在不更改已部署`master`分支的情况下对项目进行修改。现在有了一个分支，是时候创建一个文件并进行第一次提交了。

当您在GitHub上完成创建或更改文件后，滚动到页面底部。然后找到“提交新文件”部分。

在第一个字段中，键入提交消息。提交消息应该简要地告诉贡献者您正在向文件引入的更改。

规则：

- 不要以句点结束提交消息
- 将提交消息保持在50个字符以内。如有必要，在扩展描述窗口中添加额外的细节。它位于主题行的正下方。
- 使用主动态 For example, "add" instead of "added" and "merge" instead of "merged".
- 将您的提交视为表达引入更改的意图。

工作：

1. Create a new file on this branch, in a 

  ```
  _posts
  ```

   folder called 

  ```
  0000-01-02-chan-shaw.md
  ```

  . You can do so using 

  this shortcut

   or manually as follows:

  - Return to the "Code" tab
  - In the branch drop-down, select "chanshaw"
  - Click **Create new file**
  - In the "file name" field, type `_posts/0000-01-02-chan-shaw.md`. Entering the `/` in the filename will automatically place your file in the `_posts` directory.

2. When you’re done naming the file, add the following content to your file:

   ```
   ---
   layout: slide
   title: "Welcome to our second slide!"
   ---
   Your text
   Use the left arrow to go back!
   ```

3. After adding the text, you can commit the change by entering a commit message in the text-entry field below the file edit view. For guidelines on commit messages, check out the **Commits 101** drop-down, just above these instructions

4. When you’ve entered a commit message, click **Commit new file**

#### 第六步 Open a pull request

1. Open a pull request using [this shortcut](https://github.com/chan-shaw/github-slideshow/compare/refs/heads/chanshaw?expand=1)  or manually as follows:
   - From the "Pull requests" tab, click **New pull request**
   - In the "base:" drop-down menu, make sure the "master" branch is selected
   - In the "compare:" drop-down menu, select "chanshaw"
2. When you’ve selected your branch, enter a title for your pull request. For example `Add chan-shaw's file`
3. The next field helps you provide a description of the changes you made. Feel free to add a description of what you’ve accomplished so far. As a reminder, you have: created a branch, created a file and made a commit, and opened a pull request
4. Click **Create pull request**

#### 第七步 Respond to a review

1. Click the [Files Changed tab](https://github.com/chan-shaw/github-slideshow/pull/3/files) in this pull request
2. Click on the pencil icon found on the right side of the screen to edit your newly added file
3. Replace line 5 with something new
4. Scroll to the bottom and click **Commit Changes**

#### 第八步 Merge your pull request 合并分支

1. Click **Merge pull request**
2. Click **Confirm merge**
3. Once your branch has been merged, you don't need it anymore. Click **Delete branch**.

### 学到了什么？

- 您了解了问题，拉取请求以及GitHub存储库的结构
- 你学会了分支
- 您创建了一个提交
- 您查看并回复了拉取请求评论
- 您编辑了现有文件
- 您启用了GitHub页面
- 你做了第一个贡献！ 🎉



## GitHub学习笔记二 html

### 第一步 Hosting webpage

1. Under your repository name, click [**Settings**](https://github.com/chan-shaw/intro-html/settings).
2. In the **GitHub Pages** section, use the **Select source** drop-down menu to select `master` as your GitHub Pages publishing source.
3. Click **Save**.

### 第二步 Open a pull request

1. Create a pull request. You can either [use this direct link](https://github.com/chan-shaw/intro-html/compare/master...add-index?expand=1), or go to the **Code** tab, click on **New Pull Request**, select **base: master**, and **compare: add-index**.
2. Add a descriptive title to your pull request, something like "Add the index.html file".
3. Add a descriptive body to your pull request.
4. Click **Create pull request**.

### 第三步 Add HTML document structure

1. Click on **Files Changed** to see the newly added `index.html` file.
2. Click on the 📝 to edit the file.
3. Before the existing content, add an opening `<html>` tag, and an opening `<body>` tag.
4. After the existing content, add a closing `</body>` tag, and a closing `</html>` tag.
5. In the *Commit changes* section, enter a commit message that describes what you've done.
6. Ensure you've selected *Commit directly to the add-index branch*.
7. Click on **Commit changes**.

### 第四步 Add a page title

1. Click on **Files Changed**.
2. Click on the 📝 to edit the file.
3. Place an opening `<head>` tag and an opening `<title>` tag after the first opening `html` tag, but before the `body` tag.
4. Write out a title after the opening `title` tag.
5. Place a closing `</title>` tag and a closing `</head>` tag after your new title, but before the `body` tag.
6. In the *Commit changes* section, enter a commit message that describes what you've done.
7. Ensure you've selected *Commit directly to the add-index branch*.
8. Click on **Commit changes**.

### 第五步 Merge your first pull request

1. Click on **Merge pull request** below.
2. Click on **Confirm merge**.
3. Click on **Delete branch**.

### 第六步 Add a header

1. Edit the `index.html` file in your master branch by [using this direct link](https://github.com/chan-shaw/intro-html/edit/master/index.html) or going to the **Code** tab, clicking on the `index.html` file, clicking the pencil 📝 to edit the HTML.
2. Between the body tags, add an opening `<h1>` tag, some content for the header, and a closing `</h1>`tag.
3. In the *Commit changes* section, enter a commit message that describes what you've done.
4. Ensure you've selected *Create a new branch for this commit and start a pull request*.
5. Give your branch a descriptive name, like `add-headers-and-images`.
6. Click on **Propose file change**.
7. Give your pull request a title, and a comment.
8. Click on **Create pull request.**

### 第七步 Add an image

1. Click on **Files Changed**.
2. Click on the 📝 to edit the file.
3. Place an opening `<img>` tag between the body tags. Reminder: you don't need to close an `<img>` tag!
4. Set the `src` attribute to the image you would like to display. You can use your GitHub profile picture: `https://avatars1.githubusercontent.com/u/18410964?v=4`
5. In the *Commit changes* section, enter a commit message that describes what you've done.
6. Click on **Commit changes**.

### 第八步 Merge your second pull request

1. Click on **Merge pull request** below.
2. Click on **Confirm merge**.
3. Click on **Delete branch**.

### 第九步  Create a list

1. Edit the `index.html` file in your master branch by [using this direct link](https://github.com/chan-shaw/intro-html/edit/master/index.html) or going to the **Code** tab, clicking on the `index.html` file, clicking the pencil 📝 to edit the HTML.
2. Inside the body tag, create a list, either ordered or unordered, of your favorite sites on the internet.
3. In the *Commit changes* section, enter a commit message that describes what you've done.
4. Ensure you've selected *Create a new branch for this commit and start a pull request*.
5. Give your branch a descriptive name, like `add-links-and-lists`.
6. Click on **Commit changes**.
7. Give your pull request a title, and a comment.
8. Click on **Create pull request.**

### 第十步 Add links to your list

1. Click on **Files Changed**.

2. Click on the 📝 pencil icon to edit the file.

3. In the list you just created, add a link to each of your favorite sites to their respective URLs. You can do this by adding an opening anchor tag `<a>` tag with `href` attribute with your favorite site's URL, the name of the site inside the anchor tag, and a closing anchor `</a>` tag. Here is an example of a list item with a link:

   ```
   <li><a href="https://github.com">This is a link to GitHub!</a></li>
   ```

4. In the *Commit changes* section, enter a commit message that describes what you've done.

5. Click on **Commit changes**.

### 第十一步 Merge your third pull request

1. Click on **Merge pull request** below.
2. Click on **Confirm merge**.
3. Click on **Delete branch**.

### 第十二步  Make it beautiful

1. Edit the `index.html` file in the `add-style` branch by [using this direct link](https://github.com/chan-shaw/intro-html/edit/add-style/index.html) or going to the **Code** tab, selecting the `add-style` branch, clicking on the `index.html` file, and clicking the pencil 📝 to edit.
2. Between the `<head>` tags, add the following `<link rel="stylesheet" href="style.css">`.
3. In the *Commit changes* section, enter a commit message that describes what you've done.
4. Click on **Commit changes**.

### 第十三步 Merge your final pull request

1. Click on **Merge pull request** below.
2. Click on **Confirm merge**.
3. Click on **Delete branch**.



## GitHub学习笔记三 Markdown

### Step 1: Create a Task List

1. Add a comment to this issue, using Markdown to create a list of items to complete. Your task can include any tasks you wish, or you can use this example:

   ```
   - [ ] Turn on GitHub Pages
   - [ ] Outline my portfolio
   - [ ] Introduce myself to the world
   ```

2. Use the **Preview** tab to check your markdown formatting.

3. Click **Comment**.

### Step 2: Turn on GitHub Pages

### Step 3: Add headers

#### Example

```
# This is an <h1> header, which is the largest
## This is an <h2> header
###### This is an <h6> header, which is the smallest
```

1. In this pull request, click the **Files changed** tab.
2. In the upper right corner of the file view, click the **small pencil** ✏️ icon for the file titled `_includes/01-name.md`.
3. On the **Edit file** tab, add a `#` before the content to make it an H1 Header. You can add more headers, using one to six `#` characters.
4. Above your new content, click **Preview changes**.
5. At the bottom of the page, type a short, meaningful commit message that describes the change you made to the file.
6. Click **Commit changes**.

### Step 4: Merge your headers

1. Click **Merge pull request** below.

### Step 5: Add an image

1. As you did before, edit the file in this pull request.
2. In the file, replace the content in `_includes/02-image.md` with the correct Markdown for your image of choice. Don't forget to include alt-text!
3. Commit your changes.

### Step 6: Add a profile link

### Step 8: Add a list

1. Edit the `_includes/04-lists.md` in this pull request.
2. Create a markdown list in the file. It can be ordered or unordered.
3. Commit your changes.

### Step 9: Use emphasis

Great job with those lists! Let's try something new. You can use **bold** and *italic* text in Markdown. There are a couple of ways to create emphasis.

```
*This text will be italic*
_This will also be italic_

**This text will be bold**
__This will also be bold__

_You **can** combine them_
```



## GitHub学习笔记四 GitHub Page

### Enable GitHub Pages

在本课程中，您将学习如何构建和托管[GitHub Pages](https://pages.github.com/)站点。使用GitHub页面，您可以托管[文档](https://flight-manual.atom.io/)，[简历](https://github.com/jglovier/resume-template)或任何其他您想要的静态内容等内容。

在本课程中，您将创建一个在GitHub页面上托管的博客，并学习如何：

- 启用GitHub页面
- 使用[Jekyll](https://jekyllrb.com/)，一个静态站点生成器
- 使用主题和内容自定义Jekyll网站

1. Under your repository name, click [**Settings**](https://github.com/chan-shaw/github-pages-with-jekyll/settings)
2. In the "GitHub Pages" section, use the Select source drop-down menu to select the `master branch` as your GitHub Pages publishing source.
3. Click **Save**

### Step 2: Customize your homepage

您可以通过向`index`文件或`README.md`文件添加内容来自定义主页。GitHub Pages首先查找`index`文件。如果`index`找到文件，GitHub Pages使用文件中的内容来创建主页。如果`index`找不到文件，则使用该文件`README.md`创建主页。

1. Navigate to the **Code** tab of this repository, and browse to the `index.md` file.
2. In the upper right corner, click the ✏️ pencil icon to open the file editor.
3. Type the content you want on your homepage and remember, [you can use Markdown](https://help.github.com/articles/basic-writing-and-formatting-syntax/) to format your content.
4. Scroll to the bottom, type a commit message, and click **Create a new branch for this commit and start a pull request**.
5. Open a pull request.

### Step 4: Adding a Theme

1. Under your repository name, click [**Settings**](https://github.com/chan-shaw/github-pages-with-jekyll/settings).
2. In the "GitHub Pages" section, click **Choose a theme**.
3. Click the theme you want to use and then click **Select theme**.

### Step 5: Customize Site Details

#### Getting your page blog ready

GitHub added the theme you selected to your `_config.yml` file. Jekyll uses the `_config.yml` file to store settings for your site, like your theme, as well as reusable content like your site title and GitHub handle.

You can check out the `_config.yml` file on the **Code** tab of your repository.



Let's change the `_config.yml` so it's a perfect fit for your new blog. First, we need to use a blog-ready theme. For this activity, we will use a theme named `minima`.

1. Navigate to the **Code** tab of this repository, and browse to the `_config.yml` file.
2. In the upper right corner, click ✏️ to open the file editor.
3. Change `theme:` to **minima** and modify the other configuration variables such as `title:`, `author:`, and `description:` to customize your site.
4. Click **Create a new branch for this commit and start a pull request**.
5. Open a pull request.

### Step 6: Create a blog post

⚠️ Do not merge yet! ⚠️

-  It is in the `_posts` directory
-  The filename starts with the correct date formatting: YYYY-MM-DD
-  The filename includes a post title
-  The filename uses the `.md` extension
-  The filename follows Jekyll's [specific filename requirements](https://jekyllrb.com/docs/posts/#creating-post-files).

1. On the "Code" tab, select your `chan-shaw-page` branch
2. Click **Create new file**
3. Name the file `_posts/YYYY-MM-DD-title.md`, but replace the `YYYY-MM-DD` with today's date, and `title`with the title you'd like to use for your first blog post
   - If your blog post date doesn't follow the correct date convention, you'll receive an error and your site won't build. For more information, see "[Page build failed: Invalid post date](https://help.github.com/articles/page-build-failed-invalid-post-date/)".
4. Type a quick draft of your blog post. Remember, you can always edit it later
5. Commit your changes to your branch

#### Edit your file

1. Click the "Files Changed" tab in this pull request
2. Click on the pencil icon on the right side of the screen
3. Make adjustments based on the above errors
4. Scroll to the bottom, and commit your changes

### Step 7: Add blog post metadata

Now that you've created your Jekyll blog post file, we can add syntax to it. The syntax Jekyll files use is called YAML front matter. It goes at the top of your file and looks something like this:

```
---
title: "Welcome to my blog"
date: 2019-01-20
---
```

This example adds a title and date to your blog post. There are other useful things you could add here in the future like layouts, categories, or any other logic that is useful to you. For more information about configuring front matter, see the [Jekyll front matter documentation](https://jekyllrb.com/docs/frontmatter/).

1. Click the "Files Changed" tab in this pull request

2. Scroll past the config file, and find the file that you created

3. Click on the pencil icon on the right side of the screen

4. Type the following content at the top of your blog post:

   ```
   ---
   title: "YOUR-TITLE"
   date: YYYY-MM-DD
   ---
   ```

5. Replace YOUR-TITLE with the title for your blog post

6. Replace YYYY-MM-DD with today's date

7. Commit your changes to your branch

## GitHub学习笔记五 Managing merge conflicts

### Step 1：Create a normal pull request

1. On the **Code** tab, click the `_data/skills.yml` file
2. In the upper right corner of the file view, click the ✏️ icon to open the file editor
3. In the file, add a skill or two that you're proficient in
4. Scroll to the bottom of the page and select the option to "Create a new branch for this commit and start a pull request"
5. Replace the default patch branch name with `change-skills`
6. Select **Propose file change**
7. In the "Leave a comment" field of the pull request, describe the change you made
8. Click **Create pull request**
9. 🚧 Don't merge yet! 🚧 Refresh the pull request to receive the next comment/instructions.

### Step 2: Merge the pull request

### Step 3: Resolve a simple conflict

他们都创建了分支，对`_config.yml`文件进行了更改，并打开了拉取请求。一个拉取请求合并到`master`没有问题，但现在另一个拉取请求显示冲突。

1. At the bottom of the page in the "This branch has conflicts that must be resolved" section of the Pull Request, click the **Resolve conflicts** button

2. Look for the highlighted sections that begins with `<<<<<<< update-config` and ends with `>>>>>>> master`. These markers are added by Git to show you the content that is in conflict

3. Remove the changes made on the master branch by deleting all of the content below the `=======` and above `>>>>>>> master`

4. Next, remove the merge conflict markers by deleting the following lines:

   ```
   <<<<<<< update-config
   =======
   >>>>>>> master
   ```

5. **Optional:** If you'd like, you can edit the `_config.yml` file with your own information. Change any of the lines within the file, even outside of where the markers were. More about this below

6. With the merge conflict markers removed, click **Mark as resolved**

7. Finally, click **Commit merge**

### Step 4: Merge the first resolved pull request

### Step 5a: Create your own conflict

1. Click on the **Files changed** tab in this pull request
2. Click the ✏️ found in the top right-hand corner of the `_data/education.yml` file that had been previously modified
3. Modify the content in the `degree:`, `uni:`, `year:`, and `summary:` lines
4. Scroll to the bottom of the page and enter a commit message for your change
5. Click the **Commit changes** button, making sure the "Commit directly to the **add-education** branch" option is selected

### Step 5b: Merging similar changes

1. Click on [#3](https://github.com/chan-shaw/merge-conflict/pull/3)
2. Follow the instructions in that pull request to approve it

### Step 6: Conflicting change

1. In the pull request status window below, click **Add your review**
2. When the pull request review window displays, select **Approve** and click **Submit review**

### Step 7: Resolve conflicts you created

1. In the "This branch has conflicts that must be resolved" section of the pull request, click **Resolve conflicts**.

### Step 9: Resolve conflicts in the Advanced Conflicts pull request

1. Click **Resolve conflicts**
2. On the left, you will notice two files listed: `_data/experience.yml` and `_data/interests.yml`. Let's start with `experience.yml`
3. Notice there are two distinct sets of conflict markers. This is because multiple sections of the file were modified on both branches, so Git identified the two changes within the file as two separate conflicts
4. **Optional:** Again, if you're trying to use this course to build a resume, you can resolve the conflicting files with your own information
5. With the merge conflicts resolved and the markers removed in the `experience.yml` file, click **Mark as resolved**
6. GitHub will present the next file with conflicts, `interests.yml`
7. This file has some extra merge commit markers. Simply select the Interests you would like to list and remove the others (and all those extra conflict markers)
8. When you are finished, click **Mark as resolved**
9. Click **Commit merge**

## GitHub学习笔记六 Community Starter Kit

### Step 1: Add a Repository Description

1. Access the [**Code**](https://github.com/chan-shaw/community-starter-kit/) tab of your project.
2. Click the **Edit** button located on the right side of the screen (above the green **Clone or download**button).
3. Enter a description for your project in the **Description** field.
4. (Optional) If you have a website related to your project you can add it in the **Website** field.
5. Click the **Save** button.
6. When you are finished, go ahead and **close this issue**.

### Step 2: Edit the README

1. Access the **Files changed** tab in this pull request
2. Click the ✏️ pencil icon to edit the README
3. Find the two placeholder texts labeled DESCRIPTION PLACEHOLDER and FEATURES PLACEHOLDER. Replace them with some bulleted information that may be helpful to new users
4. After editing the README file, scroll down and click the **Commit changes** button.

### Step 4: Create user documentation

精心编写的用户文档是获得出色用户体验的关键。专家说，“如果用户无法在不到一个小时的时间内弄清楚如何使用您的项目，他们就会继续前进。” 为您的项目吸引一些技术作家是值得的😉

<details open="" style="box-sizing: border-box; display: block; margin-bottom: 16px; margin-top: 0px; color: rgb(36, 41, 46); font-family: -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, Helvetica, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;; font-size: 14px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); text-decoration-style: initial; text-decoration-color: initial;"><summary style="box-sizing: border-box; display: list-item; cursor: pointer;"><font style="box-sizing: border-box; vertical-align: inherit;"><font style="box-sizing: border-box; vertical-align: inherit;">在哪里创建您的文档</font></font></summary><hr style="box-sizing: content-box; height: 0.25em; overflow: hidden; background: rgb(225, 228, 232); border: 0px; margin: 24px 0px; padding: 0px;"><h3 style="box-sizing: border-box; margin-bottom: 16px; margin-top: 24px; font-size: 1.25em; font-weight: 600; line-height: 1.25;"><font style="box-sizing: border-box; vertical-align: inherit;"><font style="box-sizing: border-box; vertical-align: inherit;">在哪里创建文档</font></font></h3><p style="box-sizing: border-box; margin-bottom: 16px; margin-top: 0px;"><font style="box-sizing: border-box; vertical-align: inherit;"><font style="box-sizing: border-box; vertical-align: inherit;">您的用户文档应该易于用户查找。</font><font style="box-sizing: border-box; vertical-align: inherit;">有些人喜欢将它放在面向外部的网站上，而其他人则会在他们的项目或存储库维基中使用docs文件夹。</font></font></p><h4 style="box-sizing: border-box; margin-bottom: 16px; margin-top: 24px; font-weight: 600; font-size: 1em; line-height: 1.25;"><font style="box-sizing: border-box; vertical-align: inherit;"><font style="box-sizing: border-box; vertical-align: inherit;">组织有序</font></font></h4><p style="box-sizing: border-box; margin-bottom: 16px; margin-top: 0px;"><font style="box-sizing: border-box; vertical-align: inherit;"><font style="box-sizing: border-box; vertical-align: inherit;">随着开源项目变得越来越流行和越来越复杂，一些维护者发现将开源项目转移到GitHub组织更容易。</font><font style="box-sizing: border-box; vertical-align: inherit;">这允许您为项目的促销网站，文档，源代码等创建单独的存储库。</font></font></p><hr style="box-sizing: content-box; height: 0.25em; overflow: hidden; background: rgb(225, 228, 232); border: 0px; margin: 24px 0px; padding: 0px;"></details>

#### 包括什么

开源项目的文档需求会有所不同，但至少应包括：

-  安装说明
-  完成基本任务
-  自定义和配置

1. Access the **Files changed** tab in this pull request
2. Click the ✏️ pencil icon to edit the `getting-started.md` document
3. Find the text marked CONFIGS PLACEHOLDER and add some example welcome messages that may be helpful to new users
4. After editing the `getting-started.md` file, scroll down and click the **Commit changes** button.



### 了解贡献者

大多数贡献者采取渐进的方式参与开源社区。它通常看起来像这样：

1. 报告错误
2. 建议一个新功能
3. 创建拉取请求

更多经验丰富的开源贡献者可能会直接创建他们的第一个拉取请求，因此将有用的模板添加到存储库，使*每个人都能*更容易地参与，无论他们以前的经验如何。

### Step 6: Adding an issue template

1. Access the [**Code**](https://github.com/chan-shaw/community-starter-kit/) tab of your project
2. Be sure to select the `add-issue-template` branch from the branch dropdown
3. Click the **Create new file** button
4. Create a file in the `.github/ISSUE_TEMPLATE` folder named `bug-report.md` (you can do this by typing `.github/ISSUE_TEMPLATE/bug-report.md` in the file name field)
5. Include headings and placeholders for the information you want to collect when a user creates a bug report
6. After editing the template file, scroll down and click the **Commit new file**

## Step 8: Add a CONTRIBUTING guide

1. Access the **Files changed** tab in this pull request
2. Click the ✏️ pencil icon to edit the CONTRIBUTING.md file
3. Find the text marked DESCRIPTION PLACEHOLDER and add a description for each label
4. After editing the CONTRIBUTING file, scroll down and click the **Commit changes** button.

### Step 9: Add labels

1. On the top right side of this pull request view, find the section titled: "Labels"
2. Click the gear icon
3. Notice the text field at the top says **Filter or create labels**
4. Type the name of the label you would like to create
5. You will see a selection that reads "Create a new label", select that option
6. Customize your label with a description and color
7. Click **Save**
8. Add your new label to this pull request!

### Step 11: Add the LICENSE

许可证在开源存储库中至关重要，因为它们设置了允许其他人如何使用，更改和为项目做出贡献的规则。事实上，如果Mona没有为她的项目添加开源许可证，那么它在技术上并不是开源的 - 在这种情况下，它可以被视为受版权保护的工作。

Mona在<https://choosealicense.com/>上对开源许可证进行了大量研究（我们也建议你这样做）。在与她的法律团队进行快速咨询后，Mona选择了简单而宽松的MIT许可证。

在[选择一个许可的网站](https://choosealicense.com/)有你可以用它来抓取许可证文本有益的复制/粘贴链接。强烈建议您不要以任何方式更改许可证，除非按照说明填写项目详细信息。

Mona已经获得了许可证，您能否确保填写项目详情？

1. Access the **Files changed** tab in this pull request
2. Click the ✏️ pencil icon to edit the LICENSE.md file
3. Replace the `[year]` and `[fullname]` placeholder text within the license and edit as needed
4. After editing the LICENSE file, scroll down and click the **Commit changes** button.

### Step 13: Add the Code of Conduct

Having a Code of Conduct is really important. Instead of re-writing the wheel, Mona wants to adopt the **Contributor Covenant Code of Conduct**.

Can you help her grab the markdown version from [the contributor covenant website](https://www.contributor-covenant.org/) and paste it in to the empty file in this pull request?

1. Copy the markdown text from the latest version on [the contributor covenant website](https://www.contributor-covenant.org/)
2. Access the **Files changed** tab in this pull request
3. Click the ✏️ pencil icon to edit the `code-of-conduct.md` file
4. Paste the markdown formatted text into the file, don't forget there is a placeholder for an email address ([INSERT EMAIL ADDRESS])
5. Scroll down and click the **Commit changes** button.

### Step 15: Help users find the project

### Adding Topics to the repository

1. On the **Code** tab, click the **Add topics** option located under the repository description
2. In the **Topics** field, type the first topic `Probot`. GitHub might also have some **Suggested** topics for this repository
3. After entering a topic (or topics), click **Done**

- 帮助其他人了解他们如何使用Mona的项目
- 认识到人们可能贡献的方式
- 建立了组织捐款的系统
- 设定对贡献者的期望
- 使Mona的项目很容易找到



## GitHub学习笔记七  Uploading your project to GitHub 将代码上传到github

### Step 1: Planning the move

### Step 2: Prepare the project

#### Add a `.gitignore`

当我们将您的项目转换为Git存储库时，它应该只包含构建或编译项目所需的源代码。除了如上所述避免二进制文件之外，您还需要保留版本控制代码中的构建工件。

为此，您将在当前项目中创建一个名为的文件`.gitignore`。Git将使用它`.gitignore`来确定在版本控制下不应跟踪哪些文件和目录。该[`.gitignore`文件](https://help.github.com/articles/ignoring-files/)存储在您的存储库中，以便与与存储库交互的任何其他用户共享忽略规则。

由于要忽略的文件取决于您使用的语言，因此开源社区为存储库中的`.gitignore`文件提供了一些很棒的模板[`github/gitignore`](https://github.com/github/gitignore)。

### Step 3: Make the move

让项目已经存储在本地使您可以相当快速地将其移动到GitHub。以下活动提供了使用各种工具将本地项目移动到GitHub的说明。选择您最熟悉的工具并进行导入😄。

### 使用命令行

1. 在命令行中，导航到项目目录。键入`git init`以将目录初始化为Git存储库。
2. 类型 `git remote add origin https://github.com/chan-shaw/github-upload.git`
3. 类型 `git add .`
4. 类型 `git commit -m "initializing repository"`
5. 键入`git push -u origin master`以将您在本地的文件推送到GitHub上的远程。（可能会要求您登录。）

### 使用GitHub桌面

1. 在GitHub Desktop中，单击添加本地存储库`File > Add a Local Repository`，然后导航到本地存储库。
2. 通过在提供的字段中键入摘要提交消息并单击**Commit to master**来创建第一个提交
3. 通过单击`Repository > Repository Settings...`GitHub上的存储库中的URL并将其粘贴到“主远程存储库（源）”字段中来添加远程。单击**保存**。
4. 单击右上角的“ **发布** ”将存储库推送到GitHub。

### 使用Visual Studio Code

1. 在Visual Studio Code 中，打开项目的文件夹。

2. 单击左侧的图标以进行**源代码管理**。

3. 在“源代码管理”面板的顶部，单击**Git图标**。

4. 如果您看到的文件与要创建的存储库匹配，请单击“ **初始化存储库”**。

5. 在单词

   CHANGES

   旁边，单击加号的符号以暂存所有更改。

   - 这是两阶段提交的一部分。您可以使用此临时功能在整个开发过程中创建有意义的提交。

6. 在“源代码管理”面板的框中，键入提交消息。像“初始提交 - 移动项目”之类的东西可以工作。

7. 单击“源代码管理”面板顶部的复选标记。

8. 打开View> Integrated Terminal下的集成终端。

9. 在命令行中，键入 `git remote add origin`

10. 在“源代码管理”面板中，单击可展开的三个点以打开选项菜单。

11. 当系统询问您是否要发布分支时，请单击“ **确定”**。























