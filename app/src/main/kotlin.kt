class PensionTrackerActivity : AppCompatActivity() {

    private lateinit var textPensionAmount: TextView
    private lateinit var textPensionDate: TextView
    private var userId: Int = -1
    private lateinit var pensionDao: PensionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pension_tracker)

        textPensionAmount = findViewById(R.id.textPensionAmount)
        textPensionDate = findViewById(R.id.textPensionDate)
        userId = intent.getIntExtra("userId", -1)
        pensionDao = AppDatabase.getInstance(this).pensionDao()

        loadPensionRecord()
    }

    private fun loadPensionRecord() {
        Executors.newSingleThreadExecutor().execute {
            var record = pensionDao.getPensionForUser(userId)
            if (record == null) {
                record = PensionRecord()
                record.userId = userId
                record.amount = 20000.0
                record.dateIssued = "2025-07-01"
                pensionDao.insertPensionRecord(record)
            }

            runOnUiThread {
                textPensionAmount.text = "₹${record.amount}"
                textPensionDate.text = record.dateIssued
            }
        }
    }
}