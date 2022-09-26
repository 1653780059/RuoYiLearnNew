package com.example.farmwork.handler;

import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.text.impl.DefaultTextCreator;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author 16537
 * @Classname KaptchaTextCreateHandler
 * @Description 验证码文本生成处理
 * @Version 1.0.0
 * @Date 2022/9/21 14:51
 */
public class KaptchaTextCreateHandler extends DefaultTextCreator {
    @Override
    public String getText() {

        Random random = new Random();
        int x = random.nextInt(10);
        int y= random.nextInt(10);
        StringBuilder stringBuilder = new StringBuilder();
        int opration = random.nextInt(2);
        switch (opration){
            case 0: stringBuilder.append(x).append("+").append(y).append("=@").append(x+y);break;
            case 1: if(x>y){
                stringBuilder.append(x).append("-").append(y).append("=@").append(x-y);break;
            }else {
                stringBuilder.append(y).append("-").append(x).append("=@").append(y-x);break;
            }
            default:break;
        }

        return stringBuilder.toString();
    }
}
