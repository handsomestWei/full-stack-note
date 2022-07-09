package dto

type DemoPack struct {
	Id   string     `json:"id"`
	Data []DemoData `json:"data"`
}

type DemoData struct {
	DataTime string `json:"dataTime"`
}
