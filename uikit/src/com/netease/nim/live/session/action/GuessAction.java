package com.netease.nim.live.session.action;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nim.live.session.extension.GuessAttachment;

/**
 * Created by hzxuwen on 2015/6/11.
 */
public class GuessAction extends BaseAction {

    public GuessAction() {
        super(R.drawable.message_plus_guess_selector, R.string.input_panel_guess);
    }

    @Override
    public void onClick() {
        GuessAttachment attachment = new GuessAttachment();
        IMMessage message;
        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
            message = ChatRoomMessageBuilder.createChatRoomCustomMessage(getAccount(), attachment);
        } else {
            message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), attachment.getValue().getDesc(), attachment);
        }

        sendMessage(message);
    }
}
