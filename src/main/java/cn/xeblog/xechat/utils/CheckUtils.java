package cn.xeblog.xechat.utils;

import cn.xeblog.xechat.domain.mo.User;
import cn.xeblog.xechat.domain.ro.MessageRO;
import cn.xeblog.xechat.enums.CodeEnum;
import cn.xeblog.xechat.exception.ErrorCodeException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 校验相关
 *
 * @author yanpanyi
 * @date 2019/3/25
 */
@Component
public class CheckUtils {

    /**
     * 撤消消息过期时间 3分钟
     */
    private static final long MESSAGE_EXPIRE_DATE = 180000;

    private static String password;

    @Value("${chatrecord.password}")
    public void setPassword(String password) {
        CheckUtils.password = password;
    }

    /**
     * 校验撤消的消息id
     *
     * @param messageId
     * @return
     */
    public static void checkMessageId(String messageId, String userId) throws ErrorCodeException {
        if (StringUtils.isEmpty(messageId)) {
            throw new ErrorCodeException(CodeEnum.INVALID_PARAMETERS);
        }

        String[] str = StringUtils.split(messageId, ':');

        if (!userId.equals(str[0])) {
            throw new ErrorCodeException(CodeEnum.INVALID_TOKEN);
        }

        // 判断消息是否过期
        if (System.currentTimeMillis() > Long.parseLong(str[1]) + MESSAGE_EXPIRE_DATE) {
            throw new ErrorCodeException(CodeEnum.MESSAGE_HAS_EXPIRED);
        }
    }

    /**
     * 判断是否是图片
     *
     * @param type
     * @return true是图片 false不是图片
     */
    public static boolean isImage(String type) {
        return ".jpg".equals(type) || ".jpeg".equals(type) || ".png".equals(type) || ".bmp".equals(type)
                || ".gif".equals(type);
    }

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public static boolean checkToken(String token) {
        return StringUtils.isEmpty(token) ? false : password.equals(DigestUtils.md5Hex(token));
    }

    /**
     * 校验用户信息
     *
     * @param user
     * @return
     */
    public static boolean checkUser(User user) {
        return null != user && StringUtils.isNotEmpty(user.getUserId());
    }

    /**
     * 校验消息内容
     *
     * @param message
     * @return
     */
    public static boolean checkMessage(String message) {
        return StringUtils.isNotEmpty(message);
    }

    /**
     * 校验图片地址
     *
     * @param image
     * @return
     */
    public static boolean checkImageUrl(String image) {
        return StringUtils.isNotEmpty(image);
    }

    /**
     * 校验消息请求对象
     *
     * @param messageRO
     * @return
     */
    public static boolean checkMessageRo(MessageRO messageRO) {
        if (messageRO == null) {
            return false;
        }

        return checkMessage(messageRO.getMessage()) || checkImageUrl(messageRO.getImage());
    }

    /**
     * 校验订阅地址
     *
     * @param subAddress
     * @return
     */
    public static boolean checkSubAddress(String subAddress) {
        return StringUtils.isNotEmpty(subAddress);
    }

    /**
     * 校验接收者
     *
     * @param receiver
     * @return
     */
    public static boolean checkReceiver(String[] receiver) {
        return ArrayUtils.isNotEmpty(receiver);
    }
}
