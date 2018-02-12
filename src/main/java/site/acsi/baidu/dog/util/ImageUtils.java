package site.acsi.baidu.dog.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import site.acsi.baidu.dog.pojo.SubImage;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/9
 */
@Slf4j
@Component
public class ImageUtils {

    /**
     * 判断是否位黑色像素
     * @param rgb rgb
     * @return 是否为黑色
     */
    boolean isBlack(int rgb) {
        Color color = new Color(rgb);
        return color.getRed() + color.getGreen() + color.getBlue() <= 300;
    }

    /**
     * 打印图片，调试时使用
     * @param image 需要打印的图片
     */
    public void printImage(BufferedImage image) {
        int h = image.getHeight();
        int w = image.getWidth();
        for (int y = 0; y < h; y++) {
            StringBuilder builder = new StringBuilder();
            for (int x = 0; x < w; x++) {
                if (isBlack(image.getRGB(x, y))) {
                    builder.append("*");
                } else {
                    builder.append(" ");
                }
            }
            log.debug(builder.toString());
        }
    }

    /**
     * 移除干扰线
     * @param img 需要处理的图片
     * @param px 判定干扰线的像素点数
     * @return 处理后的图片
     */
    public BufferedImage removeLine(BufferedImage img, int px) {
        if (img != null) {
            int width = img.getWidth();
            int height = img.getHeight();

            for (int x = 0; x < width; x++) {
                List<Integer> list = new ArrayList<>();
                for (int y = 0; y < height; y++) {
                    int count = 0;
                    while (y < height - 1 && isBlack(img.getRGB(x, y))) {
                        count++;
                        y++;
                    }
                    if (count <= px && count > 0) {
                        for (int i = 0; i <= count; i++) {
                            list.add(y - i);
                        }
                    }
                }
                if (list.size() != 0) {
                    for (Integer aList : list) {
                        img.setRGB(x, aList, Color.white.getRGB());
                    }
                }
            }

            for (int y = 0; y< height; y++ ) {
                List<Integer> list = new ArrayList<>();
                for (int x=0; x<width; x++) {
                    int count = 0;
                    while (x < width -1 && isBlack(img.getRGB(x,y))) {
                        count ++;
                        x++;
                    }
                    if (count<=px-1 && count > 0) {
                        for (int i=0; i<=count;i++) {
                            list.add(x-i);
                        }
                    }
                }
                if (list.size() != 0) {
                    for (Integer aList : list) {
                        img.setRGB(aList, y, Color.white.getRGB());
                    }
                }
            }
        }
        return img;
    }

    /**
     * 把base64图片数据转为本地图片
     *
     * @param base64ImgData base64加密的图片数据
     * @param filePath 文件路径
     * @throws IOException io 异常
     */
    public void convertBase64DataToImage(String base64ImgData, String filePath) throws IOException {
        BASE64Decoder d = new BASE64Decoder();
        byte[] bs = d.decodeBuffer(base64ImgData);
        FileOutputStream os = new FileOutputStream(filePath);
        os.write(bs);
        os.close();
    }

    /**
     * 将base64图片数据转为BufferedImage
     * @param base64ImgData base64加密的图片数据
     * @return bufferedImage
     * @throws IOException IO 异常
     */
    public BufferedImage convertBase64DataToBufferedImage(String base64ImgData) throws IOException {
        BASE64Decoder d = new BASE64Decoder();
        byte[] bs = d.decodeBuffer(base64ImgData);
        return ImageIO.read(new ByteArrayInputStream(bs));
    }

    /**
     * 图像预处理
     * @param sourceImage 图像
     * @return 处理过的图像
     */
    public BufferedImage preProcess(BufferedImage sourceImage){
        double wr = 0.299;
        double wg = 0.587;
        double wb = 0.114;

        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int[][] gray = new int[width][height];

        //灰度化
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(sourceImage.getRGB(x, y));
                int rgb = (int) ((color.getRed()*wr + color.getGreen()*wg + color.getBlue()*wb) / 3);
                gray[x][y] = rgb;
            }
        }

        BufferedImage binaryBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        //二值化
        int threshold = getOstu(gray, width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (gray[x][y] > threshold) {
                    int max = Color.WHITE.getRGB();
                    gray[x][y] = max;
                }else{
                    int min = Color.BLACK.getRGB();
                    gray[x][y] = min;
                }

                binaryBufferedImage.setRGB(x, y, gray[x][y]);
                if (x == 0 || x==width-1 || y == 0 || y == height-1) {
                    binaryBufferedImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        // 移除干扰线
        return removeLine(binaryBufferedImage, 4);
    }

    /**
     * 获得二值化图像
     * 最大类间方差法
     * @param gray 灰度
     * @param width 宽
     * @param height 高
     */
    private int getOstu(int[][] gray, int width, int height){
        int grayLevel = 256;
        int[] pixelNum = new int[grayLevel];
        // 计算所有色阶的直方图
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int color = gray[x][y];
                pixelNum[color] ++;
            }
        }

        double sum = 0;
        int total = 0;
        for (int i = 0; i < grayLevel; i++) {
            // x*f(x)质量矩，也就是每个灰度的值乘以其点数（归一化后为概率），sum为其总和
            sum += i*pixelNum[i];
            // n为图象总的点数，归一化后就是累积概率
            total += pixelNum[i];
        }
        // 前景色质量矩总和
        double sumB = 0;
        int threshold = 0;

        // 背景色权重
        double wB = 0;
        // 最大类间方差
        double maxFreq = -1.0;

        for (int i = 0; i < grayLevel; i++) {
            // wB为在当前阈值背景图象的点数
            wB += pixelNum[i];
            if (wB == 0) {
                // 没有分出前景后景
                continue;
            }
            // wF为前景色着重，wB为在当前阈值前景图象的点数
            double wF = total - wB;
            if (wF == 0) {
                // 全是前景图像，则可以直接break
                break;
            }

            sumB += (double)(i*pixelNum[i]);
            double meanB = sumB / wB;
            double meanF = (sum - sumB) / wF;
            // freq为类间方差
            double freq = wF * wB *(meanB - meanF)*(meanB - meanF);
            if (freq > maxFreq) {
                maxFreq = freq;
                threshold = i;
            }
        }

        return threshold;
    }

    /**
     * cfs进行分割,返回分割后的数组
     * @param sourceImage 源图片
     * @return 切割成若干图片
     */
    public List<BufferedImage> cfs(BufferedImage sourceImage){

        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        // 保存子图像
        List<SubImage> subImgList = new ArrayList<>();
        // 已经访问过的点
        Map<String, Boolean> trackMap = Maps.newHashMap();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = sourceImage.getRGB(x, y);
                String key = x + "-" + y;
                // 如果不是黑色，或者已经被访问过，则跳过cfg
                if (!isBlack(rgb) || trackMap.containsKey(key)) {
                    continue;
                }

                /*
                   如果黑色，且没有访问，则以此点开始进行连通域探索
                   保存当前字符块的坐标点
                 */
                SubImage subImage = new SubImage();

                // 保存当前字符块的访问队列
                LinkedList<SubImage.Point> queue = new LinkedList<>();
                queue.offer(new SubImage.Point(x, y, true));
                trackMap.put(key, true);
                subImage.getPixelList().add(new SubImage.Point(x, y, true));
                subImage.setLeft(x);
                subImage.setTop(y);
                subImage.setRight(x);
                subImage.setBottom(y);

                while(queue.size() != 0){
                    SubImage.Point tmp = queue.poll();

                    //搜寻目标的八个方向
                    int startX = (tmp.getX() - 1 < 0) ? 0 : tmp.getX()-1;
                    int startY = (tmp.getY() - 1 < 0) ? 0 : tmp.getY()-1;
                    int endX = (tmp.getX() + 1 > width - 1) ? width - 1 : tmp.getX() + 1;
                    int endY = (tmp.getY() + 1 > height - 1) ? height - 1 : tmp.getY() + 1;

                    for (int tx = startX; tx <= endX; tx++) {
                        for (int ty = startY; ty <= endY; ty++) {
                            if (tx == tmp.getX() && ty == tmp.getY()) {
                                continue;
                            }

                            key = tx + "-" + ty;
                            if (isBlack(sourceImage.getRGB(tx, ty)) && !trackMap.containsKey(key)) {
                                queue.offer(new SubImage.Point(tx, ty, true));
                                trackMap.put(key, true);
                                // 加入到路径中
                                subImage.getPixelList().add(new SubImage.Point(tx, ty, true));

                                // 更新边界区域
                                subImage.left = Math.min(subImage.getLeft(), tx);
                                subImage.top = Math.min(subImage.getTop(), ty);
                                subImage.right = Math.max(subImage.getRight(), tx);
                                subImage.bottom = Math.max(subImage.getBottom(), ty);
                            }
                        }
                    }
                }

                subImage.width = subImage.right - subImage.left + 1;
                subImage.height = subImage.bottom - subImage.top + 1;
                subImgList.add(subImage);
            }
        }

        return cfsToImage(subImgList);
    }

    private List<BufferedImage> cfsToImage(List<SubImage> subImgList){
        List<BufferedImage> bufferedImages = Lists.newArrayList();
        for (SubImage subImage : subImgList) {
            BufferedImage image = new BufferedImage(subImage.width, subImage.height, BufferedImage.TYPE_BYTE_BINARY);
            for (int x = 0; x < subImage.width; x++) {
                for (int y = 0; y < subImage.height; y++) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
            List<SubImage.Point> pixeList = subImage.getPixelList();
            for (SubImage.Point point : pixeList) {
                image.setRGB(point.x - subImage.left, point.y - subImage.top, Color.BLACK.getRGB());
            }

            // 将切割的中间图片加入到cfgList中
            bufferedImages.add(image);
        }
        return bufferedImages;
    }
}
