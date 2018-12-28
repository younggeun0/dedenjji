package dedenjji.client.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class DedenjjiClientView extends JFrame {

	private JTextArea jtaLogs;
	private JScrollPane jspLogs;
	private JButton jbConnectToServer;
	private JButton jbSend;
	private JButton jbClose;
	private JComboBox<String> jcbTeam;
	
	public DedenjjiClientView() {
		super("Dedenjji Client");
		
		String[] team = new String[]{ "¾Õ", "µÚ" };
		
		jtaLogs = new JTextArea();
		jspLogs = new JScrollPane(jtaLogs);
		jspLogs.setBorder(new TitledBorder("System Log"));
		jbConnectToServer = new JButton("Connect Server");
		jbSend = new JButton("Send");
		jbClose = new JButton("Close");
		jcbTeam = new JComboBox<String>(team);
		
		setLayout(null);
		
		jspLogs.setBounds(15, 10, 405, 200);
		jbConnectToServer.setBounds(15, 220, 130, 30);
		jcbTeam.setBounds(155, 220, 100, 30);
		jbSend.setBounds(270, 220, 70, 30);
		jbClose.setBounds(350, 220, 70, 30);
		
		add(jspLogs);
		add(jbConnectToServer);
		add(jcbTeam);
		add(jbSend);
		add(jbClose);
		
		setBounds(400, 200, 450, 300);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JTextArea getJtaLogs() {
		return jtaLogs;
	}
	public JScrollPane getJspLogs() {
		return jspLogs;
	}
	public JButton getJbConnectToServer() {
		return jbConnectToServer;
	}
	public JButton getJbSend() {
		return jbSend;
	}
	public JButton getJbClose() {
		return jbClose;
	}
	public JComboBox<String> getJcbTeam() {
		return jcbTeam;
	}
}
