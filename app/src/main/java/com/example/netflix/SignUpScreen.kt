package com.example.netflix

import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun SignUpScreen(onNavigateToLogin: ()->Unit, loginManager: LogInManager) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val auth: FirebaseAuth = Firebase.auth

    val activity = context as ComponentActivity
    val pickImageLauncher = activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
        }
    }

    fun SignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    message = "Sign-Up Successful"
                    onNavigateToLogin()
                } else {
                    message = task.exception?.message ?:"Sign-Up Failed"
                }
            }
    }

    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        ConstraintLayout (modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement =Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Sign Up", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)

                Spacer(modifier = Modifier.height(16.dp))

                imageUri?.let {
                    Image(
                        painter = painterResource(id = R.drawable.group_10),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                pickImageLauncher.launch("image/*")
                            }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            loginManager.uploadProfilePicture(imageUri!!) { downloadUrl ->
                                Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Upload Profile Picture")
                    }
                } ?: run {
                    Image(
                        painter = painterResource(id = R.drawable.group_10),
                        contentDescription = "Placeholder Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                pickImageLauncher.launch("image/*")
                            }
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Upload Profile Picture", color = Color.White, fontSize = 18.sp)

                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    label = { Text(text = "Email")},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", onTextLayout = { }) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password", onTextLayout = { }) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                Button(onClick = {
                        if (password == confirmPassword) {
                            SignUp(email, password)
                        } else {
                            message = "Passwords do not match"
                        }
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign Up")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = message, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)

            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewSignUpScreen() {
//    SignUpScreen({  })
//}