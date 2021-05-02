variable "name" {
  type = string
  default = "pathfinder-backend"
  description = "Name for resources used in this stack"
}

variable "stage_name" {
  type = string
  default = "Stage for API gateway endpoint"
}

variable "jarfile" {
  type = string
  default = "URI of code to upload"
}

variable "DB_USER" {
  type = string
  description = "Username for pathfinder profiles database"
  default = "pathfinder"
}

variable "DB_PASSWORD" {
  type = string
  description = "Password for pathfinder profiles database"
  default = "pathfinder"
}

variable "DB_URL" {
  type = string
  description = "JDBC connection string for pathfinder profiles database"
  default = "jdbc:mysql://mysql/pathfinder"
}
