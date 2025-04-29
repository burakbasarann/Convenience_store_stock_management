package com.basaran.casestudy.utils

import android.content.Context
import android.net.Uri
import com.basaran.casestudy.R
import com.basaran.casestudy.data.model.Product
import com.basaran.casestudy.data.model.Transaction
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportUtils {
    fun exportToPdf(
        context: Context,
        transactions: List<Transaction>,
        products: List<Product>,
        uri: Uri
    ) {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            val pdfWriter = PdfWriter(outputStream)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            val title = Paragraph(context.getString(R.string.transaction_report))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20f)
            document.add(title)

            val table = Table(5)
            table.setWidth(500f)

            table.addHeaderCell(context.getString(R.string.date))
            table.addHeaderCell(context.getString(R.string.type))
            table.addHeaderCell(context.getString(R.string.product))
            table.addHeaderCell(context.getString(R.string.quantity))
            table.addHeaderCell(context.getString(R.string.notes))

            transactions.forEach { transaction ->
                val product = products.find { it.id == transaction.productId }
                val productName = product?.name ?: context.getString(R.string.unknown_product)
                val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(Date(transaction.date))

                table.addCell(date)
                table.addCell(transaction.type.name)
                table.addCell(productName)
                table.addCell(transaction.quantity.toString())
                table.addCell(transaction.notes ?: "-")
            }

            document.add(table)
            document.close()
        }
    }

    fun exportToCsv(
        context: Context,
        transactions: List<Transaction>,
        products: List<Product>,
        uri: Uri
    ) {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            val writer = OutputStreamWriter(outputStream)
            val csvPrinter = CSVPrinter(
                writer,
                CSVFormat.DEFAULT
                    .withHeader(
                        context.getString(R.string.date),
                        context.getString(R.string.type),
                        context.getString(R.string.product),
                        context.getString(R.string.quantity),
                        context.getString(R.string.notes)
                    )
            )

            transactions.forEach { transaction ->
                val product = products.find { it.id == transaction.productId }
                val productName = product?.name ?: context.getString(R.string.unknown_product)
                val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(Date(transaction.date))

                csvPrinter.printRecord(
                    date,
                    transaction.type.name,
                    productName,
                    transaction.quantity,
                    transaction.notes ?: "-"
                )
            }

            csvPrinter.flush()
            csvPrinter.close()
        }
    }
} 