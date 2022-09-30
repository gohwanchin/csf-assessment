// Implement the methods in PizzaService for Task 3
// Add appropriate parameter and return type 

import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Order, OrderSummary } from "./models";

const URL = '/api/order'

@Injectable()
export class PizzaService {

  constructor(private http: HttpClient) { }

  // POST /api/order
  // Add any required parameters or return type
  createOrder(order: Order): Promise<any> {
     return firstValueFrom(this.http.post<any>(URL, order))
  }

  // GET /api/order/<email>/all
  // Add any required parameters or return type
  getOrders(email: string): Promise<Array<OrderSummary>> {
    return firstValueFrom(this.http.get<Array<OrderSummary>>(URL + `/${email}/all`)) 
  }

}
