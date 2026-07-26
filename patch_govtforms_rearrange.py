path = "app/src/main/java/com/saltech/urdocs/ui/screens/GovtFormsScreen.kt"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old = '''        // ---------- HEADER ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, GPink, RoundedCornerShape(10.dp))
                    .clickable { onNavigate("home") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = GPink
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Filled.AccountBalance,
                    contentDescription = null,
                    tint = GGreen,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.height(2.dp))
                Row {
                    Text(
                        "BODEGA",
                        color = GPink,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        " NG ",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "LINKS",
                        color = GGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Text(
                    "G O V ' T   W E B S I T E S",
                    color = GGray,
                    fontSize = 10.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, GPink, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = GPink,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ---------- SEARCH BAR ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(GCardBg)
                .border(
                    BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(GGreen, GPink))),
                    RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                tint = GGray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        "Type mo lang unang letter na hahanapin mo luv\U0001fa76",
                        color = GGray,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )
            Icon(
                Icons.Filled.Tune,
                contentDescription = null,
                tint = GPink,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.height(12.dp))'''

new = '''        // ---------- TOP ROW: back + search + heart ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, GPink, RoundedCornerShape(10.dp))
                    .clickable { onNavigate("home") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = GPink
                )
            }
            Spacer(Modifier.width(10.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GCardBg)
                    .border(
                        BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(GGreen, GPink))),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    tint = GGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            "Type mo lang unang letter na hahanapin mo luv\U0001fa76",
                            color = GGray,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
                Icon(
                    Icons.Filled.Tune,
                    contentDescription = null,
                    tint = GPink,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, GPink, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = GPink,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // ---------- LOGO / TITLE ----------
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.AccountBalance,
                contentDescription = null,
                tint = GGreen,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.height(2.dp))
            Row {
                Text(
                    "BODEGA",
                    color = GPink,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    " NG ",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    "LINKS",
                    color = GGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Text(
                "G O V ' T   W E B S I T E S",
                color = GGray,
                fontSize = 10.sp
            )
        }

        Spacer(Modifier.height(12.dp))'''

assert old in content, "OLD BLOCK NOT FOUND"
content = content.replace(old, new, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)
print("DONE")
