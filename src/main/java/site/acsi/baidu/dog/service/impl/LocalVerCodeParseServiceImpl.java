package site.acsi.baidu.dog.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import org.springframework.stereotype.Service;
import site.acsi.baidu.dog.service.IVerCodeParseService;
import site.acsi.baidu.dog.util.ImageUtils;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Acsi
 * @date 2018/2/9
 */
@Service("local")
public class LocalVerCodeParseServiceImpl implements IVerCodeParseService {

    @Resource
    private ImageUtils imageUtils;

    private Map<Integer, String> labels = Maps.newHashMapWithExpectedSize(36);

    private svm_model model;

    private static final int CHAR_NUM = 36;

    public LocalVerCodeParseServiceImpl() throws IOException {
        String labelName = "1234567890abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < CHAR_NUM; i++) {
            labels.put(i + 1, String.valueOf(labelName.charAt(i)));
        }
//        URL url = getClass().getClassLoader().getResource("svm.model");
//        Preconditions.checkNotNull(url, "无法找到svm模型");
//        model = svm.svm_load_model(new BufferedReader(new FileReader(url.getFile())));
    }

    @Override
    public String predict(String imgData) throws IOException {
        // 转化成bufferedImage
        BufferedImage image = imageUtils.convertBase64DataToBufferedImage(imgData);
        // 图像预处理
        image = imageUtils.preProcess(image);
        // 切割
        List<BufferedImage> subImgs = imageUtils.cfs(image);
        // 过滤
        List<BufferedImage> filterImgs = filter(subImgs);
        // 转成svm格式数据
        List<String> svmTest = formatSvm(filterImgs);
        // 预测
        StringBuilder result = doPredict(svmTest);

        return result.toString();
    }

    private StringBuilder doPredict(List<String> svmTest) {
        StringBuilder result = new StringBuilder();
        for(String line : svmTest) {
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            Double target = Doubles.tryParse(st.nextToken());
            Preconditions.checkNotNull(target);
            int m = st.countTokens() / 2;
            svm_node[] x = new svm_node[m];
            for (int j = 0; j < m; j++) {
                x[j] = new svm_node();
                Integer index = Ints.tryParse(st.nextToken());
                Double value = Doubles.tryParse(st.nextToken());
                Preconditions.checkNotNull(index);
                Preconditions.checkNotNull(value);
                x[j].index = index;
                x[j].value = value;
            }

            double v = svm.svm_predict(model, x);
            result.append(labels.get((int)v));

        }
        return result;
    }

    private List<String> formatSvm(List<BufferedImage> filterImgs) {
        List<String> svmTest = Lists.newArrayList();
        for (BufferedImage img : filterImgs) {
            int width = img.getWidth();
            int height = img.getHeight();
            int index = 1;
            // 默认无标号，则为-1
            StringBuilder tmpLine = new StringBuilder("-1 ");
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // 黑色点标记为1
                    int value = imageUtils.isBlack(img.getRGB(x, y)) ? 1 : 0;
                    tmpLine.append(index).append(":").append(value).append(" ");
                    index ++;
                }
            }
            svmTest.add(tmpLine + "\r\n");
        }
        return svmTest;
    }

    private List<BufferedImage> filter(List<BufferedImage> imgs) {
        List<BufferedImage> filterSortedList = new ArrayList<>();
        filterSortedList.addAll(imgs);
        filterSortedList.sort(Comparator.comparingInt(img -> -(img.getWidth() * img.getHeight())));

        filterSortedList = filterSortedList.subList(0, 4);
        List<BufferedImage> imageList = new ArrayList<>();
        List<BufferedImage> finalFilterSortedList = filterSortedList;
        imgs.forEach(img -> {
            if (finalFilterSortedList.contains(img)) {
                imageList.add(img);
            }
        });
        return imageList;
    }
}
