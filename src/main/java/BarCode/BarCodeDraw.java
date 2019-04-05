package BarCode;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import Contri.ContributorMap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class BarCodeDraw {
    ChartPanel frame1;
    public  BarCodeDraw(BarCode barCode){
        CategoryDataset dataset = getDataSet(barCode);
        JFreeChart chart = ChartFactory.createBarChart(
                "Commit条形码", // 图表标题
                "时间", // 目录轴的显示标签
                "Commit次数", // 数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向：水平、垂直
                true,           // 是否显示图例(对于简单的柱状图必须是false)
                false,          // 是否生成工具
                false           // 是否生成URL链接
        );

        //从这里开始
        CategoryPlot plot=chart.getCategoryPlot();//获取图表区域对象
        CategoryAxis domainAxis=plot.getDomainAxis();         //水平底部列表
        domainAxis.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题
        domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题
        ValueAxis rangeAxis=plot.getRangeAxis();//获取柱状
        rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));
        chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
        chart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体

        //到这里结束，虽然代码有点多，但只为一个目的，解决汉字乱码问题

        frame1=new ChartPanel(chart,true);        //这里也可以用chartFrame,可以直接生成一个独立的Frame

        //保存成图片
        String saveName = barCode.commitMsgs.get(0).getAuthorName();
        File file = new File(saveName+".png");
        try {
            ChartUtilities.saveChartAsJPEG(file, chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static CategoryDataset getDataSet(BarCode barCode) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        barCode.barNodeList.forEach((node)->{
            dataset.addValue(node.info.getCommitTimes(),"提交次数",dateFormat.format(node.date));
        });
        return dataset;
    }
    public void drawChartPanel(){
        JFrame frame=new JFrame("Java数据统计图");
        frame.add(frame1);           //添加柱形图
        frame.setBounds(50, 50, 800, 600);
        frame.setVisible(true);
    }
}
