// HeroStoryGenerator.java - External class for generating stories in multiple languages

package com.example.sainiksathimobile;

import java.util.Locale;

public class HeroStoryGenerator {
    public static String generateStory(String fullName, String rank, String serviceYearsStr, String retireYearStr, String language) {
        int serviceYears = Integer.parseInt(serviceYearsStr);
        int retireYear = Integer.parseInt(retireYearStr);
        int joinYear = retireYear - serviceYears;

        String nameOnly = fullName.split(" ")[0];

        switch (language) {
            case "Hindi":
                return rank + " " + fullName + " ने देश की सेवा " + serviceYears + " वर्षों तक की। उन्होंने " + joinYear + " में भारतीय सेना जॉइन की एक मजबूत दिल और देशभक्ति के सपने के साथ।\n\n" +
                        "कई महीनों और कई बार सालों तक वह अपने परिवार से दूर रहे। उन्होंने जन्मदिन, शादी की सालगिरह, त्योहार और स्कूल फंक्शन मिस किए। उनके बच्चे दूसरे बच्चों के पिताओं को रोज़ देखते थे, लेकिन उनके पिता बस एक फोटो फ्रेम में या कभी-कभार वीडियो कॉल पर दिखते थे। फिर भी उन्होंने कभी शिकायत नहीं की — क्योंकि उन्होंने फर्ज़ को आराम से ऊपर रखा।\n\n" +
                        "जब घर में कोई इमरजेंसी होती — किसी की मृत्यु, बीमारी, या ज़रूरत — तो उन्हें स्पॉट लीव मांगनी पड़ती। लेकिन ये लीव मिलना कभी भी पक्का नहीं था। कई बार छुट्टी न मिली, या बहुत देर से मिली। उस दर्द को शब्दों में नहीं कहा जा सकता जो उन्होंने झेला जब वो ज़रूरत के समय घर नहीं पहुंच सके।\n\n" +
                        "उनकी पत्नी भी एक मूक सैनिक बन गईं। उन्होंने बच्चों को अकेले पाला, स्कूल की मीटिंग्स अटेंड कीं, अस्पताल लेकर गईं — सब कुछ बिना किसी शिकवा के।\n\n" +
                        "बच्चे हर साल उस एक महीने की छुट्टी का बेसब्री से इंतजार करते थे, जब उनके पापा घर आते थे। उस समय में प्यार, कहानियाँ, खाना, और रात को उनके पास सोना — सब कुछ जैसे जन्नत जैसा लगता था। लेकिन फिर, जब वे वापस जाते, बच्चे चुपचाप, गर्व के आंसुओं के साथ विदा करते थे।\n\n" +
                        "परिवार के पास फ्लाइट या होटल का खर्चा नहीं था। लेकिन उन्होंने बिना रिजर्वेशन वाली ट्रेनों में घंटों खड़े रहकर, टिफिन से खाना खाकर, साथ हँसते हुए खूबसूरत यादें बनाईं।\n\n" +
                        "जब आम लोग रात को चैन से सोते थे, तब " + rank + " " + nameOnly + " −40°C की सियाचिन की बर्फीली वादियों या 50°C की गर्मी में रेगिस्तान की सुरक्षा करते थे।\n\n" +
                        "हर दिन खतरनाक था — गोलियाँ, बम, और माइंस। दोस्तों को खोते देखना आसान नहीं था। लेकिन उन्होंने देश की सुरक्षा के लिए हिम्मत नहीं छोड़ी।\n\n" +
                        "सेना के जीवन में छोटे-छोटे पल उन्हें खुशी देते थे — बेटी का लिखा खत, साथियों संग गाने, और रिटायरमेंट का सपना।\n\n" +
                        retireYear + " में " + rank + " " + fullName + " रिटायर हुए। कोई बड़ा जश्न नहीं, बस एक गर्व की चुप्पी और यादें। वर्दी तो उतर गई, लेकिन देश के लिए प्यार और अनुशासन आज भी वैसा ही है।\n\n" +
                        "अब वे युवाओं को सेवा, बलिदान और सम्मान का पाठ पढ़ाते हैं।\n\n" +
                        "🫡 Mission ReLive सलाम करता है " + rank + " " + fullName + " को और हर उस सैनिक को, जो न केवल जंग के मैदान में, बल्कि हर दिन तन्हाई, दर्द और दूरी से लड़ता रहा।\n\n" +
                        "“एक सैनिक सेवा से रिटायर हो सकता है — लेकिन देशप्रेम से कभी नहीं।”";

            case "Telugu":
                return rank + " " + fullName + " భారతదేశానికి " + serviceYears + " సంవత్సరాలు సేవలందించారు. ఆయన " + joinYear + "లో భారత సైన్యంలో చేరారు.\n\n" +
                        "ఎన్నో నెలలు, సంవత్సరాల పాటు కుటుంబం నుంచి దూరంగా గడిపారు. పుట్టిన రోజులు, పండుగలు, పాఠశాల కార్యక్రమాలు—all missed. కానీ ఆయన ఎన్నడూ ఫిర్యాదు చేయలేదు.\n\n" +
                        "ఇంట్లో అత్యవసర పరిస్థితుల్లో సెలవు అడిగినా, సమయానికి దొరకడం కష్టం. అలాంటి వేళల్లో ఇంటికి చేరలేకపోవడం అనేది మాటల్లో చెప్పలేని బాధ.\n\n" +
                        "భార్య తన బాధను దాచేసి పిల్లలను ఎదిరించారు. పాఠశాలకు తీసుకెళ్లటం, హాస్పిటల్ పర్యటనలు — అన్నీ ఒంటరిగా నిర్వహించారు.\n\n" +
                        "పిల్లలు ఒక్క నెల సెలవు కోసం ఎదురు చూస్తారు. ఆ ఒక్క నెలలో పండగ వాతావరణం. ప్రత్యేక వంటలు, కథలు, నిద్ర—all unforgettable.\n\n" +
                        "రిజర్వేషన్ లేని ట్రైన్లలో ప్రయాణం, బాగ్ మీద కూర్చోవడం, టిఫిన్ షేర్ చేయడం — ఇవే వాళ్ల జీవితంలోని అపురూప క్షణాలు.\n\n" +
                        "ఇతరులు నిద్రపోతే, " + rank + " " + nameOnly + " −40°C మంచు లేదా 50°C ఎండలో గస్తీలు వేస్తూ దేశాన్ని రక్షించారు.\n\n" +
                        "ప్రతి రోజు ప్రాణాలకే ప్రమాదం. బుల్లెట్లు, బాంబులు, మైన్లు ఎదుర్కొన్నారు.\n\n" +
                        "చిన్న చిన్న ఆనందాల్లో సంతోషం కనిపించేది — కూతురి లేఖ, సైనికులతో పాటలు, తిరిగి ఇంటికి రానున్న ఆశ.\n\n" +
                        retireYear + "లో ఆయన పదవీవిరమణ పొందారు. సందడి లేదు — కానీ అంతర్లీనంగా గర్వం ఉంది.\n\n" +
                        "🫡 Mission ReLive హృదయపూర్వక వందనం తెలిపింది: " + rank + " " + fullName + " గారికి.\n\n" +
                        "వీరుడు డ్యూటీ నుంచి రిటైర్ కావచ్చు — కానీ దేశభక్తి నుంచి కాదు.";

            case "Tamil":
                return rank + " " + fullName + " நாட்டிற்கு " + serviceYears + " ஆண்டுகள் சேவை செய்தார். " + joinYear + "ஆம் ஆண்டு இந்திய இராணுவத்தில் சேர்ந்தார்.\n\n" +
                        "பல மாதங்கள், வருடங்கள் அவர் குடும்பத்திலிருந்து தூரமாக இருந்தார். பிறந்தநாள், விழாக்கள்—all forfeited.\n\n" +
                        "வீட்டில் அவசரநிலை ஏற்பட்டால், விடுப்புக்கோர வேண்டும். ஆனால் அது கிடைக்காது.\n\n" +
                        "அவரது மனைவி வீட்டு பொறுப்புகளை அனைத்தும் ஏற்று நிர்வகித்தார்.\n\n" +
                        "பிள்ளைகள் ஆண்டுக்கு ஒருமுறை அப்பாவை எதிர்பார்க்கிறார்கள். அந்த நேரம் அவர்கள் வாழ்வின் பொற்காலம்.\n\n" +
                        "மிகுந்த கூட்டமுள்ள ரயில்களில் பயணம், பிசின் உணவு, சந்தோஷம்—all cherished.\n\n" +
                        "பிறர் தூங்கும் பொழுது, " + rank + " " + nameOnly + " −40°C பனியில் அல்லது 50°C வெப்பத்தில் பாதுகாப்பாக இருந்தார்.\n\n" +
                        "ஒவ்வொரு நாளும் உயிருக்கு ஆபத்தானது.\n\n" +
                        "சிறிய சந்தோஷங்களில் மகிழ்ச்சி — மகளின் கடிதம், நண்பர்களுடன் பாடல்கள், வீட்டிற்குச் செல்லும் கனவு.\n\n" +
                        retireYear + "ஆம் ஆண்டு ஓய்வுபெற்றார். திரளான பாராட்டுகள் எதுவும் இல்லை. ஆனால் உள்ளத்தில் பெருமை நிரம்பியது.\n\n" +
                        "🫡 Mission ReLive பெருமையுடன் வணங்குகிறது: " + rank + " " + fullName + " அவர்களுக்கு.\n\n" +
                        "ஒரு வீரர் ஓய்வுபெறலாம். ஆனால் நாட்டுப் பற்று என்றும் நிலைத்திருக்கும்.";

            default:
                return rank + " " + fullName + " served the country for " + serviceYears + " years. He joined the Indian Army in " + joinYear + " with a strong heart and a simple dream — to protect his nation.\n\n" +
                        "For months, and sometimes even years, he lived far away from his family. He missed birthdays, anniversaries, school events, and festivals. His children grew up seeing other kids with their fathers, while theirs was only seen in an old photo frame or on a video call once in a while. But he never complained. He had chosen duty over comfort.\n\n" +
                        "When something serious happened at home — a death in the family, a serious illness, or an emergency — he had to request a spot leave. But it was never guaranteed. Many times, the leave was denied or came too late. The pain of not being there when your family needs you the most? That pain never goes away.\n\n" +
                        "His wife became a silent soldier too. She took care of the home, raised the children alone, ran to school PTMs, and handled hospital visits — all without showing the tears she cried at night.\n\n" +
                        "His kids eagerly waited every year for the one-month holiday when their father would finally come home. That short time together felt like heaven. They cooked special meals, told stories, and slept beside him like they used to as toddlers. But when he had to leave again, they quietly waved goodbye, tears held back with pride.\n\n" +
                        "The family couldn’t afford expensive hotels or flights. But they made memories in crowded, unreserved train journeys — standing for hours, sitting on bags, sharing meals from tiffins, and laughing together. Those were their most precious moments — not fancy, but full of love.\n\n" +
                        "While normal families slept in peace at night, " + rank + " " + nameOnly + " stood awake, guarding snowy borders, dense jungles, or burning deserts. He lived in conditions most people wouldn’t survive — −40°C in Siachen with frozen boots, 50°C heat in Rajasthan with no electricity, toilets, or sometimes even food.\n\n" +
                        "Every single day was dangerous. He faced bullets, bombs, and landmines, and saw comrades fall beside him. Yet, he stood firm — so that the rest of us could live safely.\n\n" +
                        "And still, he found small joys in army life — like getting a handwritten letter from his daughter, singing old songs with fellow soldiers, and dreaming about coming home for good.\n\n" +
                        "In " + retireYear + ", " + rank + " " + fullName + " retired. No big party, no media, no red carpet. Just quiet pride, a folded uniform, and a lifetime of memories and scars. He may have stopped wearing his uniform, but the discipline, strength, and love for his country still lives in him every day.\n\n" +
                        "Now he spends his time teaching young minds about service, sacrifice, and respect. He believes that the world may forget dates and wars, but should never forget the sacrifices of a soldier and his family.\n\n" +
                        "🫡 Mission ReLive salutes " + rank + " " + fullName + " — and every soldier like him.\n" +
                        "Not just for fighting wars on the battlefield,\n" +
                        "but for fighting every day with distance, pain, silence, and sacrifice.\n\n" +
                        "For missing out on life’s small joys so others could live freely.\n" +
                        "For being strong when the world had no idea what he was going through.\n\n" +
                        "“A soldier may retire from service — but he never retires from love for the country.”";
        }
    }
}
