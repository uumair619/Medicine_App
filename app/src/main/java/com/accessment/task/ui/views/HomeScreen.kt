package com.accessment.task.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.accessment.task.R
import com.accessment.task.data.models.AssociatedDrug
import com.accessment.task.data.models.BaseResponseModel
import com.accessment.task.data.models.Labs
import com.accessment.task.data.models.MedicineResponse
import com.accessment.task.ui.viewmodels.HomeScreenViewModel
import com.accessment.task.ui.viewmodels.LoginViewModel
import com.accessment.task.utils.NetworkResult
import kotlinx.coroutines.delay
import java.util.Calendar


@Composable
fun HomeScreen(username: String?,
               navController: NavController,
               homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {


    LaunchedEffect(Unit) {
        homeScreenViewModel.fetchData()
    }
    val state by homeScreenViewModel.medicineResponse.observeAsState()

    if (state is NetworkResult.Loading) {
        ShimmerLoading()
    } else {
        when (state) {
            is NetworkResult.Success -> {
                val response = (state as NetworkResult.Success<BaseResponseModel<List<MedicineResponse>>>).data
                if (response != null) {
                    HomeScreenContent(
                        response = response,
                        username = username ?: ""

                    )
                } else {
                    EmptyView()
                }
            }
            is NetworkResult.Error -> {
                ErrorView()
            }
            else -> {

            }
        }
    }


}

@Composable
fun HomeScreenContent(
    username: String,
    response : BaseResponseModel<List<MedicineResponse>>
) {

    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar with email and time of day
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Welcome $username", style = MaterialTheme.typography.labelLarge)
            TimeDisplay()
        }

        // Diseases heading
        Text(
            text = "Problems",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.Gray.copy(alpha = 0.5f))
                .padding(8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
        /*LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            items(response.data){ medicine ->
                MedicationContent(medicine = medicine)
            }
        }*/

        MedicationContent(medicine = response.data.first())

    }
}

@Composable
fun MedicationContent(medicine: MedicineResponse)
{
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = medicine.name ?: "",
            style = MaterialTheme.typography.headlineLarge
        )

        if (medicine.medications.isEmpty()) {
            // Empty view
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "No Medications",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "No medication found", style = MaterialTheme.typography.headlineLarge)
            }
        } else {
            // Display a LazyColumn for medications
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Optional spacing between items
            ) {
                items(medicine.medications) { medication ->
                    MedicineCard(medicine = medication)
                }
            }
        }

        // Labs List
        Text(
            text = "Labs",
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            style = MaterialTheme.typography.headlineLarge
        )

        // Display a horizontal list for labs
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(medicine.labs) { lab ->
                LabCard(lab = lab)
            }
        }
    }
}



@Composable
fun MedicineCard(medicine: AssociatedDrug) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { },
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image rounded corner
            Image(
                painter = rememberAsyncImagePainter(model = medicine.image),
                contentDescription = "Medicine Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Medicine details
            Column {
                Text(
                    text = medicine.name
                        ?: "Unknown",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${medicine.dose} mg",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

// Lab Card (for each lab item)
@Composable
fun LabCard(lab: Labs) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .clickable { }
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(model = lab.image),
                contentDescription = "Lab Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.Black.copy(alpha = 0.2f))
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = lab.name ?: "Unknown Lab",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ShimmerLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            repeat(8) {
                ShimmerItem()
            }
        }
    }
}

// Shimmer item for each medication
@Composable
fun ShimmerItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(100.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Box(modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.3f))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier
                    .height(14.dp)
                    .fillMaxWidth(0.5f)
                    .background(Color.Gray.copy(alpha = 0.3f))
                )
            }
        }
    }
}

// Error View for handling errors
@Composable
fun ErrorView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Error",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "An error occurred, please try again.", style = MaterialTheme.typography.bodyLarge)
    }
}

// Empty View for handling empty response
@Composable
fun EmptyView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "No Data",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "No data available", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun TimeDisplay() {

    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while (true) {

            currentTime = getCurrentTime()
            delay(1000)
        }
    }

    Text(text = currentTime, style = MaterialTheme.typography.labelMedium)
}
fun getCurrentTime(): String {
    val currentTime = Calendar.getInstance()
    val hour = currentTime.get(Calendar.HOUR_OF_DAY)
    val min = currentTime.get(Calendar.MINUTE)
    val sec = currentTime.get(Calendar.SECOND)
    return "${hour}:${min}:${sec}"

}
