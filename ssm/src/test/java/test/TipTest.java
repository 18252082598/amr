package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/* 
 * java气泡提示效果 
 * @author noobjava 
 * @version 1.0 
 * @since JDK1.6(建议) 
 * 
 */
public class TipTest extends Thread {

    private Map<String, String> feaMap = null;
    

    public TipTest(String name,String release,String feature) {
        feaMap = new HashMap<String, String>();
        feaMap.put("name", name);
        feaMap.put("release",release);
        feaMap.put("feature", feature);
        super.start();
    }
    
    public void send(String name,String release,String feature) {
        new TipTest(name, release, feature);
    }

    public void run() {
        final TipWindow tw = new TipWindow(300, 220);
        tw.setTitle("用户充值提醒:");
        JPanel headPan = new JPanel();
        JPanel feaPan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel btnPan = new JPanel();
        JButton update = new JButton("确定");

        // feaPan.setBorder(BorderFactory.createMatteBorder(1, 2, 3, 0, Color.gray));
        JLabel head = new JLabel(feaMap.get("name"));
        head.setPreferredSize(new Dimension(250, 30));
        head.setForeground(Color.black);
        JTextArea feature = new JTextArea(feaMap.get("feature"));
        feature.setEditable(false);
        feature.setForeground(Color.red);
        feature.setFont(new Font("宋体", Font.PLAIN, 13));
        // feature.setBackground(Color.ORANGE);

        feature.setPreferredSize(new Dimension(280, 60));

        JScrollPane jfeaPan = new JScrollPane(feature);
        jfeaPan.setPreferredSize(new Dimension(283, 80));
        // jfeaPan.setBorder(null);
        jfeaPan.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));

        JLabel releaseLabel = new JLabel("充值时间:" + feaMap.get("release").substring(0, 19));
        releaseLabel.setForeground(Color.gray);

        feaPan.add(jfeaPan);
        feaPan.add(releaseLabel);
        headPan.add(head);
        btnPan.add(update);
        tw.add(headPan, BorderLayout.NORTH);
        tw.add(feaPan, BorderLayout.CENTER);
        tw.add(btnPan, BorderLayout.SOUTH);

        update.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(tw, "确定关闭？");
            }
        });
        tw.setAlwaysOnTop(true);
        tw.setResizable(false);
        tw.setVisible(true);
        tw.run();
    }

//    public static void main(String args[]) {
//        String name = "用户充值:";
//        String release = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        String feature = "1.含动画渐入与渐出效果\\n2.3秒后启动动画渐出效果";
//        new TipTest(name, release, feature);
//    }

}
