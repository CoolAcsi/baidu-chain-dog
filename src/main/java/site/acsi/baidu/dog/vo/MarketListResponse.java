package site.acsi.baidu.dog.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.acsi.baidu.dog.pojo.Pet;
import site.acsi.baidu.dog.vo.base.BaseResponse;

import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketListResponse extends BaseResponse{
    private List<Pet> pets;
}