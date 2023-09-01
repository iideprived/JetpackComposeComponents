package com.iideprived.jetpackcomposecomponents

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SampleText(){
    Text("This is a sample of text from IIDeprived")
}

@Composable
@Preview
private fun Preview() {
    MaterialTheme {
        SampleText()
    }
}