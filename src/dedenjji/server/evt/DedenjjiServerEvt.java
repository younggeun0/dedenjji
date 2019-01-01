package dedenjji.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import dedenjji.server.helper.DedenjjiServerHelper;
import dedenjji.server.view.DedenjjiServerView;
import sun.security.util.DisabledAlgorithmConstraints;

public class DedenjjiServerEvt extends WindowAdapter implements ActionListener, Runnable {
	
	private DedenjjiServerView dsv;
	private boolean serverFlag;
	private ServerSocket server;
	private List<DedenjjiServerHelper> listClient;
	private Thread operThread;
	
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
				dsv.getJtaLogs().append("[server]: 서버 구동 시작...\n");
				dsv.getJbServerOnOff().setText("Close");
				
				operThread = new Thread(this);
				operThread.start();
				
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

		// 서버 소켓 열기, 접속자 받기 Thread처리
		DedenjjiServerHelper dsh;
		String nick;
		Thread thClient;
		try {
			server = new ServerSocket(6000);
			while(true) {
				// 소켓생성
				dsh = new DedenjjiServerHelper();
				dsh.setClient(server.accept());
				dsh.setDsv(dsv);
				dsh.setDis(dsh.getClient().getInputStream());
				dsh.setDos(dsh.getClient().getOutputStream());
				nick = dsh.getDis().readUTF();
				dsh.setNick(nick);
				dsv.getJtaLogs().append("[server]: 서버에 "+nick+"님이 접속했습니다.\n");
				dsv.getDlm().addElement(nick);

				// client가 접속하여 생긴 socket을 리스트로 추가
				listClient.add(dsh);
				thClient = new Thread(dsh);
				thClient.start();
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	public void close() {
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
