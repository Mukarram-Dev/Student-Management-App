package com.mukarram.superioruniversity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FeeInvoice extends AppCompatActivity {

    public static final int CREATE_FILE = 1;
    TextView Name, Program, Section, Semester, Date, Amount, remAmount;
    GoogleSignInAccount acct;
    String userID;
    private PdfDocument document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_invoice);
        final Dialog invoiceDialog = new Dialog(this);
        invoiceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        invoiceDialog.setContentView(R.layout.fee_invoice_layout);


        Name = invoiceDialog.findViewById(R.id.name);
        Program = invoiceDialog.findViewById(R.id.program);
        Section = invoiceDialog.findViewById(R.id.section);
        Semester = invoiceDialog.findViewById(R.id.semester);
        Date = invoiceDialog.findViewById(R.id.date);
        Amount = invoiceDialog.findViewById(R.id.amount);
        remAmount = invoiceDialog.findViewById(R.id.remAmount);
        acct = GoogleSignIn.getLastSignedInAccount(this);

        userID=acct.getId();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference1 = reference.child("Student").child(userID);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                //getting current date
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd,yyyy");
                String formattedDate = simpleDateFormat.format(c);
                String degree = snapshot.child("Degree").getValue(String.class);
                String name = snapshot.child("personName").getValue(String.class);
                String semester = snapshot.child("Semester").getValue(String.class);
                String section = snapshot.child("Section").getValue(String.class);

                int totalFee=snapshot.child("Student Fee").getValue(Integer.class);

                int halfFee=totalFee/2;




                Name.setText(name);
                Program.setText(degree);
                Date.setText(formattedDate);
                Semester.setText(semester);
                Section.setText(section);
                Amount.setText(String.valueOf(halfFee)+" RS (Half)");
                remAmount.setText(String.valueOf(halfFee)+" RS");




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



        invoiceDialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(invoiceDialog.getWindow().getAttributes());

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        invoiceDialog.getWindow().setAttributes(layoutParams);


        Button downloadInvoice = invoiceDialog.findViewById(R.id.downloadBtn);
        invoiceDialog.show();


        downloadInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePdfFromView(invoiceDialog.findViewById(R.id.invoiceView));
            }
        });
    }

    private void generatePdfFromView(View view) {
        Bitmap bitmap = getBitmapfromView(view);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        document = new PdfDocument();
        PdfDocument.Page myPage = document.startPage(pageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(myPage);
        createFile();
    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");
        startActivityForResult(intent, CREATE_FILE);

    }

    private Bitmap getBitmapfromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);

        Drawable drawable = view.getBackground();
        if (drawable != null) {
            drawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);

        return returnedBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            if (document != null) {
                ParcelFileDescriptor pfd = null;
                try {
                    pfd = getContentResolver().openFileDescriptor(uri, "w");
                    FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                    document.writeTo(fileOutputStream);
                    document.close();
                    Toast.makeText(this, "Pdf Save Successfully", Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    try {
                        DocumentsContract.deleteDocument(getContentResolver(), uri);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }

            }
        }
    }
}