package site.acsi.baidu.dog.global;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Acsi
 * @date 2018/2/7
 */
@Component
public class DoneOrderSet {
    private Set<String> doneOrders = Sets.newConcurrentHashSet();
    public void add(String petId) {
        if (petId!=null) {
            doneOrders.add(petId);
        }
    }

    public boolean isCompleted(String petId) {
        return null != petId && doneOrders.contains(petId);
    }
}
