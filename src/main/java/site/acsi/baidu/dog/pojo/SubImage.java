package site.acsi.baidu.dog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/9
 */
@Data
public class SubImage{
    public List<Point> pixelList = Lists.newArrayList();
    public int left = 0;
    public int top = 0;
    public int right = 0;
    public int bottom = 0;
    public int width = 0;
    public int height = 0;

    @Data
    @AllArgsConstructor
    public static class Point{
        public int x;
        public int y;
        public boolean isBlack;

        public Point(int x, int y){
            this(x, y, true);
        }
    }
}
