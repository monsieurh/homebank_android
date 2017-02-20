package budget.homebank.monsieur_h.homebudget.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import budget.homebank.monsieur_h.homebudget.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        TextView t2 = (TextView) findViewById(R.id.hb);
        t2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView t1 = (TextView) findViewById(R.id.help);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                startActivity(intent);
            }
        });
        t1.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
