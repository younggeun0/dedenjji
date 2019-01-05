package dedenjji.server.evt;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
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
				
				FileDialog fd = new FileDialog(dsv, "로그 저장", FileDialog.SAVE);
				fd.setVisible(true);
				
				String fileDir = fd.getDirectory();
				String fileName = fd.getFile();
				
				Date date = new Date();
				
				File file = new File(fd.getDirectory()+"\\"+fd.getFile()+"_"+date.getTime()+".dat");
				
				try {
					BufferedWriter br = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
					br.write(dsv.getJtaLogs().getText());
					br.flush();
					JOptionPane.showMessageDialog(dsv, "로그 저장 성공");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(dsv, "로그 저장 실패");
					e1.printStackTrace();
				}
				
			} else {
				JOptionPane.showMessageDialog(dsv, "저장할 로그가 없습니다.");
			}
		}
		if (e.getSource() == dsv.getJbExit()) {
			close();
		}
	}

	public void divideTeam() {
		List<String> listTeamFront = new ArrayList<String>(); 
		List<String> listTeamBack = new ArrayList<String>();
		DedenjjiServerHelper tempSh = null;
		
		System.out.println("listClient 사이즈 : "+listClient.size());
		try {
			for (int i = 0; i < listClient.size(); i++) {
				tempSh = listClient.get(i);
					if (tempSh.getTeam().equals("앞")) {
						listTeamFront.add(tempSh.getNick());
					} else {
						listTeamBack.add(tempSh.getNick());
					}
			}
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(dsv, "유저들이 아직 팀을 선택하지 않았습니다.");
		}
		
		if (!(listTeamFront.size() == listTeamBack.size())) {
			// 팀 분배 실패
			tempSh = listClient.get(0);
			tempSh.broadcast("팀 분배에 실패했습니다.. 다시 선택해주세요.");
			dsv.getJtaLogs().append("팀 분배에 실패했습니다.\n");
			dsv.getJspLogs().getVerticalScrollBar().setValue(dsv.getJspLogs().getVerticalScrollBar().getMaximum());
		} else {
			// 팀 분배 성공
			boolean sendFrontTeamFlag = false;
			boolean sendBackTeamFlag = false;
			
			for (int i=0; i<listClient.size(); i++) {
				tempSh = listClient.get(i);
				
				if (tempSh.getTeam().equals("앞") && !sendFrontTeamFlag) {
					StringBuilder msg = new StringBuilder();
					msg.append("팀 분배에 성공했습니다. ");
					msg.append("앞을 선택한 팀원은 ");
					for(int j=0; j<listTeamFront.size(); j++) {
						if (j == listTeamFront.size()-1) {
							msg.append(listTeamFront.get(j));
						} else {
							msg.append(listTeamFront.get(j)).append(", ");
						}
					}
					msg.append(" 입니다.");
					tempSh.broadcast(msg.toString());
					sendFrontTeamFlag = true;
					dsv.getJtaLogs().append(msg.toString()+"\n");
					dsv.getJspLogs().getVerticalScrollBar().setValue(dsv.getJspLogs().getVerticalScrollBar().getMaximum());
				}
				
				if (tempSh.getTeam().equals("뒤") && !sendBackTeamFlag) {
					StringBuilder msg = new StringBuilder();
					msg.append("팀 분배에 성공했습니다. ");
					msg.append("뒤를 선택한 팀원은 ");
					for(int j=0; j<listTeamBack.size(); j++) {
						if (j == listTeamBack.size()-1) {
							msg.append(listTeamBack.get(j));
						} else {
							msg.append(listTeamBack.get(j)).append(", ");
						}
					}
					msg.append(" 입니다.");
					tempSh.broadcast(msg.toString());
					sendBackTeamFlag = true;
					dsv.getJtaLogs().append(msg.toString()+"\n");
					dsv.getJspLogs().getVerticalScrollBar().setValue(dsv.getJspLogs().getVerticalScrollBar().getMaximum());
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
				dsh = new DedenjjiServerHelper(someClient, dlmTemp, jtaTemp, cnt, dsv, listClient, this, dsv.getJspLogs());
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

	public void setDivideTeamFlag(boolean divideTeamFlag) {
		this.divideTeamFlag = divideTeamFlag;
	}
}
