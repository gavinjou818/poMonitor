package pomonitor.test;

import java.util.ArrayList;
import java.util.List;

import pomonitor.entity.EntityManagerHelper;
import pomonitor.entity.Sensword;
import pomonitor.entity.SenswordDAO;
import pomonitor.entity.User;
import pomonitor.entity.UserDAO;

public class TestUserAndSensword {

	public static void main(String[] args) {
		UserDAO ud = new UserDAO();
		User root = ud.findById(1);

		// ������д�
		String[] list_A = new String[] { "̰��", "�ϻ���ѧ", "��Ļ", "��¥", "ѹ��", "����",
				"ϲѶ", "��" };
		String[] list_B = new String[] { "����", "��", "��", "��", "��" };
		String[] list_C = new String[] { "���", "Ůѧ��" };
		List<String[]> lists = new ArrayList<String[]>();
		lists.add(list_A);
		lists.add(list_B);
		lists.add(list_C);
		SenswordDAO sd = new SenswordDAO();
		EntityManagerHelper.beginTransaction();

		for (int i = 0; i < 3; i++) {
			for (String string : lists.get(i)) {
//				Sensword sens = new Sensword(root, (3-i)+"", string);
//				sd.save(sens);
			}
		}
		EntityManagerHelper.commit();
	}
}
