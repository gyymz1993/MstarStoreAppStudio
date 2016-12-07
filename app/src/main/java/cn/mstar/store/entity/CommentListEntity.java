package cn.mstar.store.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class CommentListEntity {

    public List<CommentContent> sevallsit;
    public int evaluationCount;

    public List<CommentContent> getSevallsit() {
        return sevallsit;
    }

    public void setSevallsit(List<CommentContent> sevallsit) {
        this.sevallsit = sevallsit;
    }

    public int getEvaluationCount() {
        return evaluationCount;
    }

    public void setEvaluationCount(int evaluationCount) {
        this.evaluationCount = evaluationCount;
    }

    public class CommentContent{
        public String userpic;
        public String name;
        public String addtime;
        public String content;
        public ArrayList<String> pic;
        public String specialTitle;
        public String proNo;
        public String ordertime;

        public String getUserpic() {
            return userpic;
        }

        public void setUserpic(String userpic) {
            this.userpic = userpic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public ArrayList<String> getPic() {
            return pic;
        }

        public void setPic(ArrayList<String> pic) {
            this.pic = pic;
        }

        public String getSpecialTitle() {
            return specialTitle;
        }

        public void setSpecialTitle(String specialTitle) {
            this.specialTitle = specialTitle;
        }

        public String getProNo() {
            return proNo;
        }

        public void setProNo(String proNo) {
            this.proNo = proNo;
        }

        public String getOrdertime() {
            return ordertime;
        }

        public void setOrdertime(String ordertime) {
            this.ordertime = ordertime;
        }
    }
}
