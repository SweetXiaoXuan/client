package cn.xxtui.brand.frontBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 24593 on 2018/2/7.
 */
public class FMapBean {
    /**
     * 企业品牌
     * @param bean 品牌bean
     * @return java.util.Map
     */
    public static Map<String, Object> getEnterpriseService(final FBrandBean bean) {
        return new HashMap<String, Object>() {
            {
                put("email", bean.getEmail());
                put("phone", bean.getPhone());
                put("address", bean.getAddress());
                put("brandName", bean.getBrandName());
                put("description", bean.getDescription());
                put("businessLicensePic", bean.getBusinessLicensePic());
                put("contact", bean.getContact());
                put("companyName", bean.getCompanyName());
                put("telephone", bean.getTelephone());
                put("brandPic", bean.getBrandPic());
            }
        };
    }

    /**
     * 个人品牌
     * @param bean 品牌bean
     * @return java.util.Map
     */
    public static Map<String, Object> getPersonalService(final FBrandBean bean) {
        return new HashMap<String, Object>() {
            {
                put("email", bean.getEmail());
                put("phone", bean.getPhone());
                put("address", bean.getAddress());
                put("brandName", bean.getBrandName());
                put("description", bean.getDescription());
                put("idCardFront", bean.getIdCardFront());
                put("idCardBack", bean.getIdCardBack());
                put("idCardInHand", bean.getIdCardInHand());
                put("givename", bean.getGivename());
                put("idNumber", bean.getIdNumber());
                put("brandPic", bean.getBrandPic());
            }
        };
    }
}
