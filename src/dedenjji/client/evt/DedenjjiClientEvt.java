package dedenjji.client.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dedenjji.client.view.DedenjjiClientView;

public class DedenjjiClientEvt implements ActionListener {

	private DedenjjiClientView dcv;
	
	public DedenjjiClientEvt(DedenjjiClientView dcv) {
		this.dcv = dcv;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dcv.getJbConnectToServer()) {
			
		}
		
		if (e.getSource() == dcv.getJbClose()) {
			dcv.dispose();
		}
	}
}
