package team.metropolia.fi.consumptor.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import team.metropolia.fi.consumptor.R;

public class SettingsDialogBuilder {

    public static AlertDialog buildSettingsDialog(@NonNull Context context, @NonNull final SettingsChangeListener listener) {

        Settings.Unit currentUnit = Settings.getCurrentUnit();
        final RadioGroup radioGroup = new RadioGroup(context);
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
                            public void onClick(DialogInterface dialog, int whichButton) {

                                int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
                                RadioButton button = (RadioButton) radioGroup.findViewById(checkedRadioButton);
                                Settings.Unit unit = (Settings.Unit) button.getTag();

                                Settings.setCurrentUnit(unit);

                                listener.onSettingsChanged();
                            }
                        }
                ).setNegativeButton(R.string.button_cancel, null)
                .create();
    }

    public interface SettingsChangeListener {

        void onSettingsChanged();
    }
}
