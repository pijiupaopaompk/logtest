package com.zkys.pad.launcher.ui.homebed.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeBedBean implements Serializable {

    /**
     * code : 200
     * data : [{"deptName":"内科","number":"联通公司测试机","pageSize":20,"disabled":1,
     * "hospitalName":"东莞大朗医院","id":76,"type":55,"isDel":1,"pageNum":1},{"deptName":"内科","sex":1,
     * "hospitalizationNumber":"12580","pageSize":20,"hospitalName":"东莞大朗医院","type":55,
     * "userName":"te塔","pageNum":1,"chargeNurse":"桃子","number":"01",
     * "createTabbDate":"2019-06-25","diagnose":"测试数据","chargeDoctor":"阿狸","dietCategory":"普通饮食",
     * "disabled":1,"pushMobile":"18439711214","id":97,"isDel":1,"nursingLevel":"二级护理","age":25,
     * "tabbId":336},{"deptName":"内科","number":"99","pageSize":20,"disabled":1,
     * "hospitalName":"东莞大朗医院","id":99,"type":21,"isDel":1,"pageNum":1},{"deptName":"内科",
     * "number":"001","pageSize":20,"disabled":1,"hospitalName":"东莞大朗医院","id":101,"type":55,
     * "isDel":1,"pageNum":1},{"deptName":"内科","sex":1,"hospitalizationNumber":"456",
     * "pageSize":20,"hospitalName":"东莞大朗医院","type":23,"userName":"周瑜","pageNum":1,
     * "chargeNurse":"小乔","number":"456","createTabbDate":"2019-06-21","chargeDoctor":"大乔",
     * "dietCategory":"低盐饮食","attention":"呜呜呜","disabled":1,"pushMobile":"18439711214",
     * "headNurse":"关羽","id":103,"isDel":1,"nursingLevel":"三级护理","age":29,"tabbId":334}]
     * msg : 操作成功！
     * sign : 38831B10A33B713C967E8C50843AE1D9
     * total : 5
     */

    private int code;
    private String msg;
    private String sign;
    private int total;
    private ArrayList<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * deptName : 内科
         * number : 联通公司测试机
         * pageSize : 20
         * disabled : 1
         * hospitalName : 东莞大朗医院
         * id : 76
         * type : 55
         * isDel : 1
         * pageNum : 1
         * sex : 1
         * hospitalizationNumber : 12580
         * userName : te塔
         * chargeNurse : 桃子
         * createTabbDate : 2019-06-25
         * diagnose : 测试数据
         * chargeDoctor : 阿狸
         * dietCategory : 普通饮食
         * pushMobile : 18439711214
         * nursingLevel : 二级护理
         * age : 25
         * tabbId : 336
         * attention : 呜呜呜
         * headNurse : 关羽
         */

        private String deptName;
        private String number;
        private int pageSize;
        private int disabled;
        private String hospitalName;
        private int id;
        private int type;
        private int isDel;
        private int pageNum;
        private int sex;
        private String hospitalizationNumber;
        private String userName;
        private String chargeNurse;
        private String createTabbDate;
        private String diagnose;
        private String chargeDoctor;
        private String dietCategory;
        private String pushMobile;
        private String nursingLevel;
        private int age;
        private int tabbId;
        private String attention;
        private String headNurse;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getDisabled() {
            return disabled;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsDel() {
            return isDel;
        }

        public void setIsDel(int isDel) {
            this.isDel = isDel;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getHospitalizationNumber() {
            return hospitalizationNumber;
        }

        public void setHospitalizationNumber(String hospitalizationNumber) {
            this.hospitalizationNumber = hospitalizationNumber;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getChargeNurse() {
            return chargeNurse;
        }

        public void setChargeNurse(String chargeNurse) {
            this.chargeNurse = chargeNurse;
        }

        public String getCreateTabbDate() {
            return createTabbDate;
        }

        public void setCreateTabbDate(String createTabbDate) {
            this.createTabbDate = createTabbDate;
        }

        public String getDiagnose() {
            return diagnose;
        }

        public void setDiagnose(String diagnose) {
            this.diagnose = diagnose;
        }

        public String getChargeDoctor() {
            return chargeDoctor;
        }

        public void setChargeDoctor(String chargeDoctor) {
            this.chargeDoctor = chargeDoctor;
        }

        public String getDietCategory() {
            return dietCategory;
        }

        public void setDietCategory(String dietCategory) {
            this.dietCategory = dietCategory;
        }

        public String getPushMobile() {
            return pushMobile;
        }

        public void setPushMobile(String pushMobile) {
            this.pushMobile = pushMobile;
        }

        public String getNursingLevel() {
            return nursingLevel;
        }

        public void setNursingLevel(String nursingLevel) {
            this.nursingLevel = nursingLevel;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getTabbId() {
            return tabbId;
        }

        public void setTabbId(int tabbId) {
            this.tabbId = tabbId;
        }

        public String getAttention() {
            return attention;
        }

        public void setAttention(String attention) {
            this.attention = attention;
        }

        public String getHeadNurse() {
            return headNurse;
        }

        public void setHeadNurse(String headNurse) {
            this.headNurse = headNurse;
        }
    }
}
