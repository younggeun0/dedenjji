package dedenjji.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dedenjji.server.view.DedenjjiServerView;

public class DedenjjiServerEvt implements ActionListener {
	
	private DedenjjiServerView dsv;
	
	public DedenjjiServerEvt(DedenjjiServerView dsv) {
		this.dsv = dsv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dsv.getJbServerOnOff()) {
			
		}
		if (e.getSource() == dsv.getJbShowResult()) {
			
		}
		if (e.getSource() == dsv.getJbSaveLog()) {
			
		}
		if (e.getSource() == dsv.getJbExit()) {
			dsv.dispose();
		}
	}
}
