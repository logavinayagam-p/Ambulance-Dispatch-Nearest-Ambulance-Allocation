package com.example.ambulancedispatch.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SwaggerController {

    @GetMapping(value = {"/swagger-ui.html", "/swagger-ui", "/swagger-ui/"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String swaggerUi() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>Ambulance Dispatch - Swagger UI</title>
              <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/swagger-ui-dist@5/swagger-ui.css" />
              <link rel="icon" type="image/png" href="https://cdn.jsdelivr.net/npm/swagger-ui-dist@5/favicon-32x32.png" sizes="32x32" />
              <style>
                html { box-sizing: border-box; overflow-y: scroll; }
                *, *:before, *:after { box-sizing: inherit; }
                body { margin: 0; background: #fafafa; }
                .topbar { display: none; }
                .swagger-ui .info .title { font-family: sans-serif; }
              </style>
            </head>
            <body>
              <div id="swagger-ui"></div>
              <script src="https://cdn.jsdelivr.net/npm/swagger-ui-dist@5/swagger-ui-bundle.js"></script>
              <script src="https://cdn.jsdelivr.net/npm/swagger-ui-dist@5/swagger-ui-standalone-preset.js"></script>
              <script>
                window.onload = function() {
                  window.ui = SwaggerUIBundle({
                    url: '/v3/api-docs',
                    dom_id: '#swagger-ui',
                    deepLinking: true,
                    presets: [
                      SwaggerUIBundle.presets.apis,
                      SwaggerUIStandalonePreset
                    ],
                    layout: "BaseLayout"
                  });
                };
              </script>
            </body>
            </html>
            """;
    }

    @GetMapping(value = "/v3/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String openApiJson() {
        return """
        {
          "openapi": "3.0.1",
          "info": {
            "title": "Ambulance Dispatch & Nearest Allocation System API",
            "description": "Complete REST API for ambulances, city zones, emergency distress calls, and automated dispatch operations.",
            "version": "1.0.0"
          },
          "servers": [
            {
              "url": "http://localhost:8080",
              "description": "Local Development Server"
            }
          ],
          "paths": {
            "/api/ambulances": {
              "get": {
                "tags": ["Ambulance API"],
                "summary": "Get all ambulances",
                "responses": {
                  "200": { "description": "List of all ambulance units in fleet" }
                }
              },
              "post": {
                "tags": ["Ambulance API"],
                "summary": "Create a new ambulance",
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": { "$ref": "#/components/schemas/AmbulanceInput" }
                    }
                  }
                },
                "responses": {
                  "200": { "description": "Ambulance successfully created" }
                }
              }
            },
            "/api/ambulances/{id}": {
              "get": {
                "tags": ["Ambulance API"],
                "summary": "Get ambulance by ID",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "responses": {
                  "200": { "description": "Ambulance details" },
                  "404": { "description": "Ambulance not found" }
                }
              },
              "put": {
                "tags": ["Ambulance API"],
                "summary": "Update ambulance",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": { "schema": { "$ref": "#/components/schemas/AmbulanceInput" } }
                  }
                },
                "responses": {
                  "200": { "description": "Ambulance updated" },
                  "404": { "description": "Ambulance not found" }
                }
              },
              "delete": {
                "tags": ["Ambulance API"],
                "summary": "Delete ambulance",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "responses": {
                  "204": { "description": "Ambulance deleted" },
                  "404": { "description": "Ambulance not found" }
                }
              }
            },
            "/api/zones": {
              "get": {
                "tags": ["Zone API"],
                "summary": "Get all zones",
                "responses": {
                  "200": { "description": "List of geographical zones" }
                }
              },
              "post": {
                "tags": ["Zone API"],
                "summary": "Create a new zone",
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": { "schema": { "$ref": "#/components/schemas/ZoneInput" } }
                  }
                },
                "responses": {
                  "200": { "description": "Zone created" }
                }
              }
            },
            "/api/zones/{id}": {
              "get": {
                "tags": ["Zone API"],
                "summary": "Get zone by ID",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "responses": {
                  "200": { "description": "Zone details" },
                  "404": { "description": "Zone not found" }
                }
              },
              "put": {
                "tags": ["Zone API"],
                "summary": "Update zone",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/ZoneInput" } } }
                },
                "responses": {
                  "200": { "description": "Zone updated" }
                }
              },
              "delete": {
                "tags": ["Zone API"],
                "summary": "Delete zone",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "responses": {
                  "204": { "description": "Zone deleted" },
                  "404": { "description": "Zone not found" }
                }
              }
            },
            "/api/emergency-calls": {
              "get": {
                "tags": ["Emergency Call API"],
                "summary": "Get all emergency distress calls",
                "responses": {
                  "200": { "description": "List of emergency calls" }
                }
              },
              "post": {
                "tags": ["Emergency Call API"],
                "summary": "Log a new emergency call",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/EmergencyCallInput" } } }
                },
                "responses": {
                  "200": { "description": "Emergency call created with status PENDING" }
                }
              }
            },
            "/api/emergency-calls/{id}": {
              "get": {
                "tags": ["Emergency Call API"],
                "summary": "Get emergency call by ID",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "responses": {
                  "200": { "description": "Call details" }
                }
              },
              "delete": {
                "tags": ["Emergency Call API"],
                "summary": "Delete emergency call",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "responses": {
                  "204": { "description": "Call deleted" }
                }
              }
            },
            "/api/emergency-calls/{id}/dispatch": {
              "post": {
                "tags": ["Dispatch API"],
                "summary": "Find and assign nearest ambulance to emergency call",
                "description": "Executes nearest ambulance allocation algorithm. First checks incident zone, then adjacent proximity zones.",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "responses": {
                  "200": { "description": "Dispatch result message" }
                }
              }
            },
            "/api/emergency-calls/{id}/complete": {
              "post": {
                "tags": ["Dispatch API"],
                "summary": "Complete emergency call and release ambulance",
                "description": "Marks call as COMPLETED, frees assigned ambulance back to AVAILABLE status, and updates dispatch log.",
                "parameters": [{ "name": "id", "in": "path", "required": true, "schema": { "type": "integer" } }],
                "responses": {
                  "200": { "description": "Completion result message" }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "AmbulanceInput": {
                "type": "object",
                "properties": {
                  "ambulanceNumber": { "type": "string", "example": "AMB101" },
                  "driverName": { "type": "string", "example": "Kavitha" },
                  "driverPhone": { "type": "string", "example": "9843210987" },
                  "ambulanceType": { "type": "string", "example": "Advanced Life Support" },
                  "status": { "type": "string", "example": "AVAILABLE" }
                }
              },
              "ZoneInput": {
                "type": "object",
                "properties": {
                  "zoneName": { "type": "string", "example": "Zone F" },
                  "zoneCode": { "type": "string", "example": "ZONE_F" },
                  "description": { "type": "string", "example": "North East Hub" }
                }
              },
              "EmergencyCallInput": {
                "type": "object",
                "properties": {
                  "callerName": { "type": "string", "example": "Rajesh" },
                  "callerPhone": { "type": "string", "example": "9443322110" },
                  "emergencyType": { "type": "string", "example": "Cardiac" },
                  "emergencyLocation": { "type": "string", "example": "Main Road Junction" }
                }
              }
            }
          }
        }
        """;
    }
}