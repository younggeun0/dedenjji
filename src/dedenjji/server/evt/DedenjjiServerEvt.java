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
	private boolean divideTeamFlag;
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
				dsv.getJtaLogs().append("서버 구동 시작...\n");
				dsv.getJbServerOnOff().setText("Close");
				
				try {
					server = new ServerSocket(6000);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(dsv, "서버 구동 실패");
					e1.printStackTrace();
				}
				
				serverThread = new Thread(this);
				serverThread.start();
			} else {
				// server off
				dsv.getJbServerOnOff().setText("Open");
				JOptionPane.showMessageDialog(dsv, "서버를 종료합니다..");
				close();
			}
		}
		if (e.getSource() == dsv.getJbShowResult()) {
			// 클라이언트들의 send값을 읽고 같은 send값끼리 팀을 나누되 쌍이 안맞으면 실패 결과 반환
			// 쌍이 맞으면 성공 결과 반환
			if (divideTeamFlag) {
				divideTeam();
			} else {
				JOptionPane.showMessageDialog(dsv, "팀을 나눌 준비가 안되었습니다.");
			}
		}
		if (e.getSource() == dsv.getJbSaveLog()) {
			// system log를 .dat파일로 출력하는 버튼
			// 객체화하여 읽을 수 없는 내용으로 출력
			if (!dsv.getJtaLogs().getText().isEmpty()) {
				
			} else {
				JOptionPane.showMessageDialog(dsv, "저장할 로그가 없습니다.");
			}
		}
		if (e.getSource() == dsv.getJbExit()) {
			dsv.dispose();
			close();
		}
	}

	public void divideTeam() {
		List<String> listTeamFront = new ArrayList<String>(); 
		List<String> listTeamBack = new ArrayList<String>();
		DedenjjiServerHelper tempSh = null;
		for (int i = 0; i < listClient.size(); i++) {
			tempSh = listClient.get(i);
			// 팀인원이 짝수일테고 결과가 size/2 개의 길이로 리스트에 들어간다면
			if (tempSh.getTeam().equals("앞")) {
				listTeamFront.add(tempSh.getNick());
			} else {
				listTeamBack.add(tempSh.getNick());
			}
		}
		
		if (listTeamFront.size() == listTeamBack.size()) {
			// 팀 분배 성공
			for (int i=0; i<listClient.size(); i++) {
				tempSh = listClient.get(i);
				tempSh.broadcast("팀 분배에 실패했습니다.. 다시 선택해주세요.");
			}
		} else {
			// 팀 분배 실패
			for (int i=0; i<listClient.size(); i++) {
				tempSh = listClient.get(i);
				// 여기서 팀에 따라 분배///////////////////////////////////////////////////
				if (tempSh.getTeam().equals("앞")) {
					tempSh.broadcast("팀 분배에 성공했습니다. 같은 팀원은\n");
					for(int j=0; j<listTeamFront.size(); j++) {
						tempSh.broadcast(listTeamFront.get(j).getN);
					}
				}
				
			}
		}
	}

	@Override
	public void run() {
		try {
			Socket someClient = null;
			DedenjjiServerHelper dsh = null;
			DefaultListModel<String> dlmTemp = dsv.getDlm();
			JTextArea jtaTemp = dsv.getJtaLogs();
			for(int cnt=1;;cnt++) {
				someClient = server.accept();
				dsh = new DedenjjiServerHelper(someClient, dlmTemp, jtaTemp, cnt, dsv, listClient, divideTeamFlag);
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
