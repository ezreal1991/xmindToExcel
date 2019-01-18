package utils;

import com.trans.enty.TreeElement;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author JIAYU_WANG
 */
public class TransUtil {
    /**
     * 获取所有的子节点
     * @param root 根节点
     * @param elementList 所有节点
     */
    public static void getAllChildren(Element root, List<Element> elementList) {
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            List<Element> elements = element.selectNodes("*");
            if (elements.size() > 0) {
                getAllChildren(element, elementList);
            } else {
                elementList.add(element);
            }
        }
    }

    /**
     * 获取节点的pid
     * @param element 节点
     * @return 节点的pid
     */
    public static String getPID(Element element) {
        Element pElement = element.getParent();
        String pID;
        if (pElement.attributeValue("id") == null || "".equals(pElement.attributeValue("id"))) {
            pID = getPID(pElement);
        }else {
            pID = pElement.attributeValue("id");
        }
        return pID;
    }

    /**
     * 获取所有的叶子节点
     * @param treeElements 所有节点
     * @param pIDs  所有pid
     * @param IDs   所有id
     * @return      所有叶子节点
     */
    public static List<TreeElement> getLeafNode(List<TreeElement> treeElements, List<String> pIDs, List<String> IDs){
        List<String> leafID = new ArrayList<>();
        List<TreeElement> leafElements = new ArrayList<>();

        // 把IDs的对象，添加到leafID
        for (String id:IDs
                ){
            leafID.add(id);
        }

        // 把不是叶子节点的去掉
        for (String id:IDs
                ) {
            for (String pID:pIDs
                    ) {
                if (id.equals(pID)){
                    leafID.remove(id);
                }
            }
        }

        // 获取叶子节点
        for (String id:leafID
                ) {
            for (TreeElement treeElement:treeElements
                    ) {
                if(id.equals(treeElement.getId())){
                    leafElements.add(treeElement);
                }
            }
        }
        return leafElements;
    }

    /**
     * 获取满足用例格式的List
     * @param treeElement 当前节点
     * @param list  满足用例格式的List
     * @param treeElements  所有节点
     */
    public static void getList(TreeElement treeElement, List list, List<TreeElement> treeElements) {
        for (TreeElement element:treeElements
                ) {
            if (treeElement.getPid().equals(element.getId())){
                list.add(element);
                getList(element,list,treeElements);
            }
        }
    }
}
