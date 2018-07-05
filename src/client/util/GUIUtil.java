package client.util;

import java.awt.*;

/**
 * @program: Java-Course-Design-im
 * @description: 界面居中显示
 * @author: 尹傲雄
 * @create: 2018-05-21 23:41
 **/
public class GUIUtil {

    /**
    * @Description: 使界面居中的函数
    * @Param: [comp]
    * @return: void
    * @Author: 尹傲雄
    * @Date: 2018/5/21
    */
    public static void toCenter(Component comp){
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rec=ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        comp.setLocation(((int)rec.getWidth()-comp.getWidth())/2, ((int)rec.getHeight()-comp.getHeight())/2);
    }

}
