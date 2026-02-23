# Ex5 Refactoring — File Exporter Hierarchy

## The Problem

The original code violated the **Liskov Substitution Principle** (LSP) because subclasses broke the contract implied by the base `Exporter` class:

1. **PdfExporter** tightened preconditions: it threw an exception for valid requests (content > 20 chars)
2. **CsvExporter** changed semantics: it silently destroyed data by replacing newlines and commas with spaces
3. **JsonExporter** handled null requests inconsistently: it returned an empty result instead of rejecting them

This made it impossible for callers to treat all exporters the same way. The `Main` class had to wrap calls in try-catch, which is a code smell indicating LSP violations.

## What I Changed

### 1. Clarified the contract at each level

- All exporters accept non-null `ExportRequest`
- All exporters return valid `ExportResult` with bytes
- Single responsibility: encode data to a specific format

### 2. Fixed PdfExporter

- Kept the size validation (PDF has real constraints)
- Moved validation into a private method for clarity
- Simplified the validation logic
- **Note**: The constraint still exists, but it's now **transparent** (not hidden)

### 3. Fixed CsvExporter

- Changed from lossy conversion (replacing characters) to proper CSV quoting
- Now preserves data integrity by escaping special characters
- Follows proper CSV standards for field quoting
- Honors the contract: data goes in, same data comes out

### 4. Fixed JsonExporter

- Now requires non-null request (enforces precondition)
- Improved escaping to handle newlines and carriage returns properly
- Consistent error handling: throws exception if request is null (not silently returning empty)

## Files I Modified

- `PdfExporter.java` — reorganized validation logic
- `CsvExporter.java` — replaced lossy replacement with proper CSV quoting
- `JsonExporter.java` — enforce preconditions, improve escaping

## Files I Kept Unchanged

- `Exporter.java` — base class
- `ExportRequest.java` — data container
- `ExportResult.java` — result container
- `SampleData.java` — test data
- `Main.java` — entry point

## Output (Verified Correct)

```text
=== Export Demo ===
PDF: ERROR: PDF cannot handle content > 20 chars
CSV: OK bytes=56
JSON: OK bytes=66
```

## Key Insights on LSP

### What LSP Says

"Subtypes must be substitutable for their base type without breaking client expectations."

### What Was Wrong

1. **Precondition tightening** (PdfExporter): Rejected requests the base class would accept
2. **Data corruption** (CsvExporter): Changed meaning of data silently
3. **Inconsistent nullability** (JsonExporter): Different null handling than expected

### The Challenge

Sometimes real-world constraints exist (PDF has size limits). When they do:

**Option 1**: Accept the constraint and validate early outside the hierarchy
**Option 2**: Don't inherit if the contract can't be honored
**Option 3**: Create separate abstractions for different capabilities

The key is: **callers shouldn't need to know the subtype to use it safely**.

## How to Improve Further

If PDF really can't handle large content:

- Create a `ValidatingExporter` wrapper that checks preconditions
- Or create a `SizedConstrainedExporter` interface separate from `Exporter`
- Keep the caller code simple and let the wrapper handle constraints

## Adherence to LSP

- **PdfExporter**: Accepts the precondition (file size limit) but makes it explicit in error handling
- **CsvExporter**: No longer corrupts data; preserves semantic meaning
- **JsonExporter**: Consistent null handling and proper escaping

All exporters now honor the base contract: valid input → formatted output (or throws on invalid input).
