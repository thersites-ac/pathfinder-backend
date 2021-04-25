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

