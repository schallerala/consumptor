package team.metropolia.fi.consumptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/17/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class SettingsDialogBuilder {

    public static AlertDialog buildSettingsDialog(Context context) {

        Settings.Unit currentUnit = Settings.getCurrentUnit();
        RadioGroup radioGroup = new RadioGroup(context);
        int padding = (int) context.getResources().getDimension(R.dimen.settings_radio_group_padding);

        radioGroup.setPadding(padding, padding, padding, padding);

        for (Settings.Unit unit : Settings.Unit.values()) {

            RadioButton button = new RadioButton(context);
            button.setText(unit.getDescriptionResId());
            button.setId(unit.ordinal());
            button.setTag(unit);

            radioGroup.addView(button);
        }

        radioGroup.check(currentUnit.ordinal());

        return new AlertDialog.Builder(context)
                .setTitle(R.string.display_units)
                .setView(radioGroup)
                .setPositiveButton(R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
//                                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
//                                int checkedRadioButton = 0;
//                                try {
//                                    checkedRadioButton = radioGroup.getCheckedRadioButtonId();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                int i = 0;
//                                switch (checkedRadioButton) {
//                                    case R.id.a2s:
//                                        datasource.updateIcon(i, itemid);
//                                        break;
//                                    case R.id.android:
//                                        i = 1;
//                                        datasource.updateIcon(i, itemid);
//                                        break;
//                                }
                            }
                        }).setNegativeButton(R.string.button_cancel, null)
                .create();
    }
}
