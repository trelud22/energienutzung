# Energienutzung in Österreich

A Spring Boot REST API serving Austrian energy usage data, broken down by year, region, sector, and fuel type. Data originates from [Statistik Austria / StatCube](https://statcube.at/statistik.at/ext/statcube/jsf/tableView/tableView.xhtml) and is loaded either from pre-converted JSON or directly from raw CSV files.

## Data Structure

The data follows a four-level hierarchy:

```
Year
└── Region
    └── Sector (e.g. Iron and steel)
        └── Fuel (e.g. Hard coal, Natural gas)
            ├── Space and water heating
            ├── Process heat <200 °C
            ├── Process heat >200 °C
            ├── Stationary engines
            ├── Traction
            ├── Lighting and computing
            └── Electrochemical purposes
```

Energy values are stored in GWh (gigawatt-hours) and may be `null` where no consumption was recorded for that category.

**Example JSON snippet:**
```json
[
  {
    "year": 2024,
    "regions": [
      {
        "region_name": "Styria",
        "sectors": [
          {
            "sector_name": "Iron and steel",
            "fuels": [
              {
                "fuel_name": "Hard coal",
                "Space and water heating": 28.4,
                "Process heat <200 °C": 42.1,
                "Process heat >200 °C": 215.6,
                "Stationary engines": 6.2,
                "Traction": null,
                "Lighting and computing": null,
                "Electrochemical purposes": null
              }
            ]
          }
        ]
      }
    ]
  }
]
```

## Data Ingestion

On startup, the application loads data via one of two modes controlled by `CSVToJson.READ_CSV`:

- **CSV mode** (`READ_CSV = true`): Reads raw `.csv` files from `resources/2005-2024/`, parses region columns, sector rows, and fuel sub-rows, and persists everything directly to the database.
- **JSON mode** (`READ_CSV = false`): Reads a pre-converted `data.json` from the classpath and deserializes it into the entity hierarchy.

## API Endpoints

All endpoints are under `/energienutzung`.

| Method | Path | Description |
|--------|------|-------------|
| GET | `/years/` | All years with full nested data |

Planned endpoints (total consumption aggregations):

- By sector → by year → by fuel
- By sector → by fuel
- By year → by fuel
- By fuel

## Collaboration

This service provides data to two downstream teams:

- **Marcel & Joshi** – consume region/sector breakdowns
- **Gabriel & Felix** – consume fuel-type aggregations

## Tech Stack

- Java 21 + Spring Boot
- Spring Data JPA (Hibernate)
- Lombok
- Jackson (JSON deserialization)
- H2 / relational DB (via JPA)
