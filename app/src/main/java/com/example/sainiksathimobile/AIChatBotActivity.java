package com.example.sainiksathimobile;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
import android.content.ActivityNotFoundException;

public class AIChatBotActivity extends AppCompatActivity {

    private LinearLayout chatLayout;
    private EditText userInput;
    private Button sendButton;

    private String lastBotContext = null; // 🧠 Track context

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat_bot);

        chatLayout = findViewById(R.id.chatLayout);
        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);

        Button btnVoice = findViewById(R.id.btnVoice);
        btnVoice.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your thoughts...");

            try {
                startActivityForResult(intent, 101);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Speech not supported", Toast.LENGTH_SHORT).show();
            }
        });

        sendButton.setOnClickListener(v -> {
            String input = userInput.getText().toString().trim();
            if (!input.isEmpty()) {
                addMessage("You", input);
                respondToUser(input);
                userInput.setText("");
            }
        });

        addMessage("Bot", "Hello! I'm your Mental Health Assistant 🤝\nTell me how you're feeling today.");
    }

    private void addMessage(String sender, String message) {
        TextView textView = new TextView(this);
        textView.setText(sender + ": " + message);
        textView.setPadding(10, 10, 10, 10);
        textView.setBackgroundResource(sender.equals("Bot") ? R.drawable.bot_bubble : R.drawable.user_bubble);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        params.setMarginStart(sender.equals("Bot") ? 10 : 100);
        textView.setLayoutParams(params);

        chatLayout.addView(textView);
    }

    private void respondToUser(String input) {
        String lower = input.toLowerCase();
        String response = "";

        if (lastBotContext != null) {
            switch (lastBotContext) {
                case "emotional_help_options":
                    if (lower.contains("breathe") || lower.contains("breathing")) {
                        response = "🧘‍♂️ Let's try it! Breathe in deeply... hold... and exhale slowly. Repeat this 5 times. Feel a bit better?";
                    } else if (lower.contains("call")) {
                        response = "📞 You can call the helpline at 18005990019 anytime. They’re here for you.";
                    } else if (lower.contains("read") || lower.contains("uplifting")) {
                        response = "📖 Here's something uplifting: 'You’ve survived 100% of your bad days. That’s proof of your strength.'";
                    } else {
                        response = "I didn't quite get that. Would you like to try deep breathing, call someone, or read something positive?";
                        // Keep context
                        return;
                    }
                    lastBotContext = null; // Reset context after handling
                    addMessage("Bot", response);
                    return;
            }
        }

        // Default logic (no prior context)
        if (lower.contains("sad") || lower.contains("depressed")) {
            response = "I'm really sorry you're feeling that way 💔. Would you like to:\n• Try a 2-minute deep breathing?\n• Call a counselor?\n• Read something uplifting?";
            lastBotContext = "emotional_help_options";
        } else if (lower.contains("anxious") || lower.contains("stress")) {
            response = "Stress can be heavy. Try:\n• A guided meditation 🌿\n• Listening to calming sounds\n• Talking to someone you trust";
        } else if (lower.contains("happy") || lower.contains("good")) {
            response = "That's wonderful to hear! 🎉 Keep doing what works for you. I'm here whenever you need me.";
        } else if (lower.contains("lonely") || lower.contains("alone")) {
            response = "You're not alone 💛. Let’s connect you to someone. Want to chat with a counselor?";
        } else {
            response = "Thank you for sharing that. Would you like me to suggest a relaxation technique or play calming music?";
        }

        addMessage("Bot", response);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = result.get(0);
            userInput.setText(spokenText);
            addMessage("You", spokenText);
            respondToUser(spokenText);
        }
    }
}
