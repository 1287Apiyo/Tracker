
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryDetailsActivity extends AppCompatActivity {

    private TextView textCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        textCategoryName = findViewById(R.id.textCategoryName);

        // Get category name from intent
        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        textCategoryName.setText(categoryName);
    }
}
