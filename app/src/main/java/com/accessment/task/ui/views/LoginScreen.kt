package com.accessment.task.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.accessment.task.BaseActivity
import com.accessment.task.R
import com.accessment.task.ui.viewmodels.LoginViewModel
import com.accessment.task.utils.NetworkResult

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel())  {

    var email by remember { mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val loginResponse by loginViewModel.loginResponse.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Main UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                onClick = {
                    // Hide the keyboard when clicking outside
                    keyboardController?.hide()
                },
                indication = null, // Remove the click effect
                interactionSource = remember { MutableInteractionSource() } // Required to disable ripple
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // App name
        Text(
            text = stringResource(id =  R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Email/Username field
        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text(text = stringResource(id =  R.string.email_or_username)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = stringResource(id =  R.string.password)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = errorMessage.isNotEmpty()
        )
        if (password.length < 8) {
            Text(
                text = stringResource(id =  R.string.password_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Login button
        val error2 = stringResource(id = R.string.password_error_2)
        Button(
            onClick = {
                if (password.length >= 8) {
                    loginViewModel.login(email, password)
                } else {
                    errorMessage =   error2
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = email.isNotEmpty() && password.length >= 8
        ) {
            Text(text = stringResource(id = R.string.login))
        }


        LaunchedEffect(loginResponse) {
            when (val response = loginResponse) {

                is NetworkResult.Loading -> {
                    isLoading = true
                }
                is NetworkResult.Success -> {
                    isLoading = false
                    navController.navigate("home/${email}")
                }
                is NetworkResult.Error -> {
                    isLoading = false

                }
                else -> Unit
            }
        }

        // Show the processing dialog if loading
        if (isLoading) {
            ProcessingDialog()
        }


    }
}

@Preview
@Composable
fun LoginScreenPreview()
{
    LoginScreen(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessingDialog() {

        AlertDialog(
            onDismissRequest = { /* Disable dismiss by clicking outside */ },
            title = {

            },
            text = {
                Text(stringResource(id = R.string.please_wait))
            },
            confirmButton = {

            }
        )

}

@Composable
fun ProcessingDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            CircularProgressIndicator()
        }
    }
}
