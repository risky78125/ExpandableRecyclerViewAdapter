package lanou3g.com.expandableadapter.entity;

import java.util.List;

/**
 * Created by Risky57 on 2016/12/28.
 * 实体类,json数据在项目的根目录下有.
 * <p>但是是私有地址,可以看一下结构,这个接口是不能使用的</p>
 */

public class GiftEntity {

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        private List<CategoriesBean> categories;

        public List<CategoriesBean> getCategories() {
            return categories;
        }

        public void setCategories(List<CategoriesBean> categories) {
            this.categories = categories;
        }

        public static class CategoriesBean {

            private String name;
            private List<SubcategoriesBean> subcategories;

            @Override
            public String toString() {
                return "CategoriesBean{" +
                        "name='" + name + '\'' +
                        '}';
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<SubcategoriesBean> getSubcategories() {
                return subcategories;
            }

            public void setSubcategories(List<SubcategoriesBean> subcategories) {
                this.subcategories = subcategories;
            }

            public static class SubcategoriesBean {

                private String icon_url;
                private String name;

                @Override
                public String toString() {
                    return "SubcategoriesBean{" +
                            "name='" + name + '\'' +
                            '}';
                }

                public String getIcon_url() {
                    return icon_url;
                }

                public void setIcon_url(String icon_url) {
                    this.icon_url = icon_url;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
