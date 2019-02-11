/**
 * 请勿将俱乐部专享资源复制给其他人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目文档与设计资源、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多
 * 
 * JFinal 俱乐部是五年以来首次寻求外部资源的尝试，以便于有资源创建更加
 * 高品质的产品与服务，为大家带来更大的价值，所以请大家多多支持，不要将
 * 首次的尝试扼杀在了摇篮之中
 */

package com.jfinal.club.common;

import com.jfinal.club._app.mall.MallAppController;
import com.jfinal.config.Routes;
import com.jfinal.club.common.upload.UploadController;
import com.jfinal.club.document.DocumentController;
import com.jfinal.club.download.DownloadController;
import com.jfinal.club.feedback.FeedbackController;
import com.jfinal.club.index.IndexController;
import com.jfinal.club.login.LoginController;
import com.jfinal.club.my.favorite.FavoriteController;
import com.jfinal.club.my.friend.MyFriendController;
import com.jfinal.club.my.feedback.MyFeedbackController;
import com.jfinal.club.my.like.LikeController;
import com.jfinal.club.my.message.MessageController;
import com.jfinal.club.my.newsfeed.NewsFeedController;
import com.jfinal.club.my.project.MyProjectController;
import com.jfinal.club.my.setting.MySettingController;
import com.jfinal.club.my.share.MyShareController;
import com.jfinal.club.project.ProjectController;
import com.jfinal.club.reg.RegController;
import com.jfinal.club.share.ShareController;
import com.jfinal.club.user.feedback.UserFeedbackController;
import com.jfinal.club.user.friend.UserFriendController;
import com.jfinal.club.user.newsfeed.UserNewsFeedController;
import com.jfinal.club.user.project.UserProjectController;
import com.jfinal.club.user.share.UserShareController;

/**
 * 小程序接口路由
 */
public class FrontRoutes extends Routes {

	public void config() {
		setBaseViewPath("/_view");
		
		add("/", IndexController.class, "/index");
		add("/login", LoginController.class);

		//add("/login/wxlogin", LoginController.class);
		add("/mallApp", MallAppController.class);




	}
}
