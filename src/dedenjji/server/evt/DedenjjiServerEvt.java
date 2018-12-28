package dedenjji.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import dedenjji.server.view.DedenjjiServerView;

public class DedenjjiServerEvt implements ActionListener {
	
	private DedenjjiServerView dsv;
	private boolean serverFlag;
	private ServerSocket server;
	private List<Socket> listClient;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public DedenjjiServerEvt(DedenjjiServerView dsv) {
		this.dsv = dsv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dsv.getJbServerOnOff()) {
			serverFlag = !serverFlag;
			if (serverFlag) {
				// server on
				dsv.getJtaLogs().append("서버 구동 시작...\n");
				dsv.getJbServerOnOff().setText("Close");
				
				/*try {
					// 소켓생성
					server = new ServerSocket(6000);
					// client가 접속하여 생긴 socket을 리스트로 추가
					listClient.add(server.accept());
					
				} catch (IOException ie) {
					ie.printStackTrace();
				} finally {
					if (server != null) { 
						try {
							server.close();
						} catch (IOException ie) {
							ie.printStackTrace();
						}
					}
				}*/
			} else {
				// server off
				dsv.getJbServerOnOff().setText("Open");
				dsv.getJtaLogs().append("서버를 종료하였습니다...\n");
				/*try {
					close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}*/
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
	
	public void close() throws IOException {
		if (server != null) {
			server.close();
		}
		for (int i=0; i<listClient.size(); i++) {
			if (listClient.get(i) != null) {
				listClient.get(i).close();
			}
		}
		if (dis != null) {
			dis.close();
		}
		if (dos != null) {
			dos.close();
		}
	}
}
