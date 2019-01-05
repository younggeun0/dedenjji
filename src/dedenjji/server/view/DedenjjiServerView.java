package dedenjji.server.view;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import dedenjji.server.evt.DedenjjiServerEvt;

@SuppressWarnings("serial")
public class DedenjjiServerView extends JFrame {

	private JList<String> jlClients;
	private DefaultListModel<String> dlm;
	private JScrollPane jspClients;
	private JTextArea jtaLogs;
	private JScrollPane jspLogs;
	private JButton jbServerOnOff;
	private JButton jbShowResult;
	private JButton jbSaveLog;
	private JButton jbExit;
	
	public DedenjjiServerView() {
		super("Dedenjji Server");
		
		dlm = new DefaultListModel<String>();
		jlClients = new JList<String>(dlm);
		jspClients = new JScrollPane(jlClients);
		jspClients.setBorder(new TitledBorder("Users"));
		jtaLogs = new JTextArea();
		jtaLogs.setEditable(false);
		jspLogs = new JScrollPane(jtaLogs);
		jspLogs.setBorder(new TitledBorder("System Log"));
		jbServerOnOff = new JButton("Open");
		jbShowResult = new JButton("Show Result");
		jbSaveLog = new JButton("Save Log");
		jbExit = new JButton("Exit");
		
		setLayout(null);
		
		jspLogs.setBounds(20, 10, 280, 300);
		jspClients.setBounds(310, 10, 100, 300);
		jbServerOnOff.setBounds(20, 320, 80, 30);
		jbShowResult.setBounds(110, 320, 120, 30);
		jbSaveLog.setBounds(240, 320, 90, 30);
		jbExit.setBounds(340, 320, 70, 30);
		
		add(jspLogs);
		add(jspClients);
		add(jbServerOnOff);
		add(jbShowResult);
		add(jbSaveLog);
		add(jbExit);
		
		DedenjjiServerEvt dse = new DedenjjiServerEvt(this);
		jbServerOnOff.addActionListener(dse);
		jbShowResult.addActionListener(dse);
		jbSaveLog.addActionListener(dse);
		jbExit.addActionListener(dse);
		
		setBounds(1200, 200, 445, 400);
		setResizable(false);
		setVisible(true);
	}

	public JList<String> getJlClients() {
		return jlClients;
	}
	public JTextArea getJtaLogs() {
		return jtaLogs;
	}
	public JButton getJbServerOnOff() {
		return jbServerOnOff;
	}
	public JButton getJbShowResult() {
		return jbShowResult;
	}
	public JButton getJbSaveLog() {
		return jbSaveLog;
	}
	public JButton getJbExit() {
		return jbExit;
	}
	public JScrollPane getJspClients() {
		return jspClients;
	}
	public JScrollPane getJspLogs() {
		return jspLogs;
	}
	public DefaultListModel<String> getDlm() {
		return dlm;
	}
	public void setDlm(DefaultListModel<String> dlm) {
		this.dlm = dlm;
	}
}
