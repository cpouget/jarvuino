const int DIGITAL_PIN_NUMBERS = 14; // Change 14 if you have a different number of pins.

boolean digitalPinListening[DIGITAL_PIN_NUMBERS]; // Array used to know which pins on the Arduino must be listening.
int digitalPinListenedValue[DIGITAL_PIN_NUMBERS]; // Array used to know which value is read last time.

char fullInput[128];
boolean incomingCommand = false;

void blink(int n, int _delay) {
  for(int i = 0; i < n; i++) {
    digitalWrite(13, HIGH);
    delay(_delay);
    digitalWrite(13, LOW);
    delay(_delay);
  }
}

void setup() {
  // initialize serial: (this is general code you can reuse)
  Serial.begin(115200);

  //set to false all listen variable
  int index = 0;
  for (index = 0; index < DIGITAL_PIN_NUMBERS; index++) {
    digitalPinListening[index] = false;
    digitalPinListenedValue[index] = -1;
  }

  pinMode(13, OUTPUT);
}

void loop() {
  if(incomingCommand) {
    char* tokens[32];

    char* token = strtok(fullInput, "/");

    int numberOfTokens = 0;

    while (token) {
      tokens[numberOfTokens++] = strdup(token);
      token = strtok(NULL, "/");
    }

    if(!strcmp("d-write", tokens[0])) {
      String pin = tokens[1];
      String value = tokens[2];

      digitalWrite(pin.toInt(), value.toInt());
    } 

    if(!strcmp("d-read", tokens[0])) {
      String pin = tokens[1];

      int value = digitalRead(pin.toInt());

      Serial.println(value);
    }
    
    // clear all
    memset(fullInput,0,sizeof(fullInput));
    incomingCommand = false;

    for(int i = 0; i < numberOfTokens; i++)
      free(tokens[i]);
  }

}

// Reads 4 times and computes the average value
int highPrecisionAnalogRead(int pin) {
  int value1 = analogRead(pin);
  int value2 = analogRead(pin);
  int value3 = analogRead(pin);
  int value4 = analogRead(pin);

  int retvalue = (value1 + value2 + value3 + value4) / 4;
}

/*
  SerialEvent occurs whenever a new data comes in the
 hardware serial RX.  This routine is run between each
 time loop() runs, so using delay inside loop can delay
 response.  Multiple bytes of data may be available.
 This is general code you can reuse.
 */
void serialEvent() {
  if (Serial.available() > 0) {
    char c = (char) Serial.read();

    if (c == ':') {
      Serial.readBytesUntil('\n', fullInput, 128);
      
      Serial.println("ack");
      Serial.flush();
      incomingCommand = true;
    }
  }
}


















