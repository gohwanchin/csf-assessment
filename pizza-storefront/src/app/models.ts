export interface Order{
    name: string
    email: string
    size: number
    base: string
    sauce: string
    toppings: Array<any>
    comments: string
}

export interface OrderSummary{
    orderId: number
    name: string
    email: string
    amount: number
}