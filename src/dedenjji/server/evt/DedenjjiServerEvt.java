package dedenjji.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import dedenjji.server.helper.DedenjjiServerHelper;
import dedenjji.server.view.DedenjjiServerView;

public class DedenjjiServerEvt extends WindowAdapter implements ActionListener, Runnable {
	
	private DedenjjiServerView dsv;
	private boolean serverFlag;
	private ServerSocket server;
	private List<DedenjjiServerHelper> listClient;
	private Thread serverThread;
	
	public DedenjjiServerEvt(DedenjjiServerView dsv) {
		this.dsv = dsv;
		listClient = new ArrayList<DedenjjiServerHelper>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dsv.getJbServerOnOff()) {
			serverFlag = !serverFlag;
			if (serverFlag) {
				// server on
				serverThread = new Thread(this);
				serverThread.start();
			} else {
				// server off
				dsv.getJbServerOnOff().setText("Open");
				dsv.getJtaLogs().append("[server]: 서버를 종료하였습니다...\n");
				close();
			}
		}
		if (e.getSource() == dsv.getJbShowResult()) {
			// 모든 클라이언트들의 요청을 받았을 때 누를 수 있는 버튼
			// 클라이언트들의 send값을 읽고 같은 send값끼리 팀을 나누되 쌍이 안맞으면 실패 결과 반환
			// 쌍이 맞으면 성공 결과 반환
		}
		if (e.getSource() == dsv.getJbSaveLog()) {
			// system log를 .dat파일로 출력하는 버튼
			// 객체화하여 읽을 수 없는 내용으로 출력
		}
		if (e.getSource() == dsv.getJbExit()) {
			dsv.dispose();
		}
	}
	
	@Override
	public void run() {
		try {
			server = new ServerSocket(6000);
			dsv.getJtaLogs().append("[server]: 서버 구동 시작...\n");
			dsv.getJbServerOnOff().setText("Close");
			
			Socket someClient = null;
			DedenjjiServerHelper dsh = null;
			DefaultListModel<String> dlmTemp = dsv.getDlm();
			JTextArea jtaTemp = dsv.getJtaLogs();
			for(int cnt=1;;cnt++) {
				someClient = server.accept();
				dsh = new DedenjjiServerHelper(someClient, dlmTemp, jtaTemp, cnt, dsv, listClient);
				
				listClient.add(dsh);
				dsh.start();
			}
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(dsv, "소켓 생성 실패");
			ie.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (server != null) {
				server.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		dsv.dispose();
	}
	@Override
	public void windowClosed(WindowEvent e) {
		close();
	}
}
