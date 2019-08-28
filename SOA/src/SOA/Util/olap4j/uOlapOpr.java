package SOA.Util.olap4j;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.olap4j.*;
import org.olap4j.metadata.Member;

import java.util.ArrayList;
import java.util.List;

import static SOA.Util.Model.StaticObj.Chr;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: QZY
 * \* Date: 2019/8/27
 * \* Time: 16:20
 * \
 */
public class uOlapOpr {

    public Document ExecuteMdxXML(String ConnNo, String Mdx) throws Exception {
        Document document = null;
        OlapConnection olapConn = TOlapConnPool.getOlapConnection(ConnNo);
        try{
            document = this.ExecuteMdxXML(olapConn, Mdx);
        }finally{
            TOlapConnPool.returnOlapConnection(ConnNo,olapConn);
        }
        return document;
    }
    /**
     * 执行Olap4J
     * @param Mdx
     * @throws Exception
     */
    public Document ExecuteMdxXML(OlapConnection olapConn,String Mdx) throws Exception {
        CellSet cellSet = null;
        Document document = DocumentHelper.createDocument();
        Element element = document.addElement("xml");
        element.addAttribute("Type", "OLAPRDATA");
        try{
            OlapStatement stamt = olapConn.createStatement();
            cellSet = stamt.executeOlapQuery(Mdx);
            List<CellSetAxis> SetAxes = cellSet.getAxes();
            CellSetAxis columnsAxis = SetAxes.get(Axis.COLUMNS.axisOrdinal());
            Element ColHead = element.addElement("ColHead");
            for (Position position : columnsAxis.getPositions()){
                Element Fieldvalue = ColHead.addElement("Col");
                List<Member> MemList = position.getMembers();
                for (int i=0;i<MemList.size();i++) {
                    Member measure = MemList.get(i);
                    Fieldvalue.addAttribute("C" + i, measure.getName());
                    Fieldvalue.addAttribute("C" + i + "_Id", measure.getUniqueName());
                    Fieldvalue.addAttribute("C" + i + "_LevelDepth",String.valueOf(measure.getDepth()));
                }
            }
            if(SetAxes.size() == 2){
                int cellOrdinal = 0;
                CellSetAxis rowsAxis = SetAxes.get(Axis.ROWS.axisOrdinal());
                Element RowHead = element.addElement("RowHead");
                Element Fieldsvalue = element.addElement("Values");
                for (Position rowPosition : rowsAxis.getPositions()) {
                    Element Fieldvalue1 = RowHead.addElement("Row");
                    List<Member> MemList = rowPosition.getMembers();
                    for (int i=0;i<MemList.size();i++) {
                        Member member = MemList.get(i);
                        Fieldvalue1.addAttribute("R" + i, member.getName());
                        Fieldvalue1.addAttribute("R" + i + "_Id", member.getUniqueName());
                        Fieldvalue1.addAttribute("R" + i + "_LevelDepth",String.valueOf(member.getDepth()));
                    }
                    Element Fieldvalue2 = Fieldsvalue.addElement("Value");
                    List<Position> PosList = columnsAxis.getPositions();
                    for (int j=0;j<PosList.size();j++) {
                        Position ColPosition = PosList.get(j);
                        Cell cell = cellSet.getCell(cellOrdinal);
                        List<Integer> coordList = cellSet.ordinalToCoordinates(cellOrdinal);
                        assert coordList.get(0) == rowPosition.getOrdinal();
                        assert coordList.get(1) == ColPosition.getOrdinal();
                        ++cellOrdinal;
                        String Value = cell.getFormattedValue();
                        Fieldvalue2.addAttribute("C" + j, Value.indexOf(".#I")>-1?"":Value);
                    }
                }
                List<Position> rowPosition = rowsAxis.getPositions();
                if(rowPosition.size()>0){
                    int Rsize = rowPosition.get(0).getMembers().size();
                    int Csize = columnsAxis.getPositions().size();
                    checkNULL(element,Rsize,Csize);
                }
            }else if(SetAxes.size() == 1){
                Element RowHead = element.addElement("RowHead");
                Element Fieldvalue1 = RowHead.addElement("Row");
                Fieldvalue1.addAttribute("R0", "");
                Fieldvalue1.addAttribute("R0_Id", "");
                Fieldvalue1.addAttribute("R0_LevelDepth","0");
                Element Fieldsvalue = element.addElement("Values");
                Element Fieldvalue2 = Fieldsvalue.addElement("Value");
                List<Position> PosList = columnsAxis.getPositions();
                for (int j=0;j<PosList.size();j++){
                    Position ColPosition = PosList.get(j);
                    Cell cell = cellSet.getCell(ColPosition.getOrdinal());
                    String Value = cell.getFormattedValue();
                    Fieldvalue2.addAttribute("C" + j, Value.indexOf(".#I")>-1?"":Value);
                }
            }
        }catch (Exception e) {
            throw new Exception(e.getMessage() + Chr(13)+Chr(10) + " Mdx: " + Mdx );
        }
        return document;
    }
    /**
     * 判断未定义，未填写的维度是否值为NULL
     * @param element
     * @param Rsize
     * @param Csize
     */
    @SuppressWarnings("unchecked")
    private void checkNULL(Element element,int Rsize,int Csize){
        Element RowHead = element.element("RowHead");
        Element Fieldsvalue = element.element("Values");

        List<Element> Rows = RowHead.elements();
        List<Element> vals = Fieldsvalue.elements();
        for(int j=0;j<Rsize;j++){
            boolean tag = false;
            List<Element> RomRows  = new ArrayList<Element>();
            List<Element> RomVals  = new ArrayList<Element>();
            for(int i = 0;i<Rows.size();i++){
                Element Row = Rows.get(i);
                Element Val = vals.get(i);
                String name = Row.attributeValue("R"+j);
                if("未定义".equals(name)||"未填写".equals(name)){
                    for(int k = 0;k<Csize;k++){
                        String val = Val.attributeValue("C"+k);
                        if("".equals(val)||"0".equals(val)||"0%".equals(val)||"0.00".equals(val)){
                        }else{
                            tag = true;
                        }
                        if(tag){
                            break;
                        }
                    }
                    if(tag){
                        break;
                    }else{
                        RomRows.add(Row);
                        RomVals.add(Val);
                    }
                }
            }
            if(tag){
                RomRows.clear();
                RomVals.clear();
                continue;
            }else if(RomRows.size()>0){
                for(int l = 0;l<RomRows.size();l++){
                    Element Row = RomRows.get(l);
                    Element Val = RomVals.get(l);
                    RowHead.remove(Row);
                    Fieldsvalue.remove(Val);
                }
                RomRows.clear();
                RomVals.clear();
            }
        }


    }
}
