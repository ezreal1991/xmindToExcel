package com.trans.enty;

/**
 * @author JIAYU_WANG
 * @TreeElement     自定义节点对象
 * @id               节点id
 * @content         内容
 * @pid              父节点ID
 */
public class TreeElement {
    private String id;
    private String content;
    private String pid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "TreeElement{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}
