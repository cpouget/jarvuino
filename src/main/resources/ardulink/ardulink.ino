#include <LiquidCrystal.h>

char fullInput[64];
boolean incomingCommand;
char* tokens[16];
char* errorMsg;

LiquidCrystal lcd(12, 11, 7, 6, 5, 4);

void blink(int n, int _delay) {
  for (int i = 0; i < n; i++) {
    digitalWrite(13, HIGH);
    delay(_delay);
    digitalWrite(13, LOW);
    delay(_delay);
  }
}

int parseInput(char* input, char** tokens) {
  char* token = strtok(input, "/");

  int numberOfTokens = 0;

  while (token) {
    tokens[numberOfTokens++] = strdup(token);
    token = strtok(NULL, "/");
  }

  return numberOfTokens;
}

void handleError(const char* errorMsg) {
  Serial.print("err: ");
  Serial.println(errorMsg);
  Serial.flush();
}

void digitalFunctions(int numTokens, char** tokens) {
  if (!strcmp("dig", tokens[0])) {

    if (!strcmp("mode", tokens[1])) {
      int value = atoi(tokens[3]);

      if (value == 0) {
        pinMode(atoi(tokens[2]), value);
      } else if (value == 1) {
        pinMode(atoi(tokens[2]), value);
      } else if (value == 2) {
        pinMode(atoi(tokens[2]), value);
      } else {
        handleError("pin-mode: invalid mode");
      }
    }

    if (!strcmp("write", tokens[1])) {
      digitalWrite(atoi(tokens[2]), atoi(tokens[3]));
    }

    if (!strcmp("read", tokens[1])) {
      int value = digitalRead(atoi(tokens[2]));

      Serial.println(value);
    }
  }
}

void analogFunctions(int numTokens, char** tokens) {
  if (!strcmp("ana", tokens[0])) {

    if (!strcmp("ref-volt", tokens[1])) {
      int refVolt = atoi(tokens[2]);

      if (refVolt == 0) {
        analogReference(DEFAULT);
      } else if (refVolt == 1) {
        analogReference(INTERNAL);
      } else if (refVolt == 2) {
//        analogReference(INTERNAL1V1);
      } else if (refVolt == 3) {
//        analogReference(INTERNAL2V56);
      } else if (refVolt == 4) {
        analogReference(EXTERNAL);
      } else {
        handleError("ref-volt: invalid ref-volt mode");
      }
    }

    if (!strcmp("write", tokens[1])) {
      analogWrite(atoi(tokens[2]), atoi(tokens[3]));
    }

    if (!strcmp("read", tokens[1])) {
      int value = analogRead(atoi(tokens[2]));

      Serial.println(value);
    }
  }
}

void extendedIOFunction(int numTokens, char** tokens) {
  if (!strcmp("advio", tokens[0])) {
    if (!strcmp("no-tone", tokens[1])) {
      noTone(atoi(tokens[2]));
    }

    if (!strcmp("tone", tokens[1])) {
      if (numTokens == 3) {
        tone(atoi(tokens[2]), atoi(tokens[3]), atol(tokens[4]));
      } else if (numTokens == 2) {
        tone(atoi(tokens[2]), atoi(tokens[3]));
      }
    }

    if (!strcmp("s-in", tokens[1])) {
      byte value = shiftIn(atoi(tokens[2]), atoi(tokens[3]), atoi(tokens[4]));

      Serial.println(value);
    }

    if (!strcmp("s-out", tokens[1])) {
      shiftOut(atoi(tokens[2]), atoi(tokens[3]), atoi(tokens[4]),
               atoi(tokens[5]));
    }

    if (!strcmp("p-in", tokens[1])) {
      if (numTokens == 3) {
        long value = pulseIn(atoi(tokens[2]), atoi(tokens[3]), atol(tokens[4]));

        Serial.println(value);
      } else {
        long value = pulseIn(atoi(tokens[2]), atoi(tokens[3]));

        Serial.println(value);
      }
    }
  }
}

void setup() {
  Serial.begin(115200);

  incomingCommand = false;

  lcd.begin(16, 2);
  lcd.clear();
}

void loop() {
  if (incomingCommand) {
    incomingCommand = false;

    lcd.clear();
    lcd.home();
    lcd.print(fullInput);

    int numberOfTokens = parseInput(fullInput, tokens);

    digitalFunctions(numberOfTokens, tokens);
    analogFunctions(numberOfTokens, tokens);
    extendedIOFunction(numberOfTokens, tokens);

    // clear all
    memset(fullInput, 0, sizeof(fullInput));

    for (int i = 0; i < numberOfTokens; i++)
      free(tokens[i]);
  }
}

void serialEvent() {
  if (Serial.available() > 0) {
    char c = (char) Serial.read();

    if (c == ':') {
      Serial.readBytesUntil('\n', fullInput, 64);

      Serial.println("ack");
      Serial.flush();

      incomingCommand = true;
    }
  }
}

