# Import libraries
import dash
import dash_table
import dash_core_components as dcc
import dash_bootstrap_components as dbc
import dash_html_components as html
import pandas as pd
from dash.dependencies import Input, Output, State
from datetime import date, timedelta
import csv
import numpy as np
import plotly.graph_objects as go

external_stylesheets = ['assets/style.css']
app = dash.Dash(__name__, external_stylesheets=external_stylesheets)
server = app.server

# collect data for the user
calories_in = {}
calories_out = {}
daily_calories = {}

# food data - create dictionary {food:calories}
food = {}
filename = "food.csv"
with open(filename, 'r') as data:
    first_line = data.readline()
    for line in csv.reader(data):
        food[line[0]] = line[1]
food_options = [{'label': name, 'value': name} for name in food.keys()]

# Separate layout parts
title = html.Div([html.H3("Milestone Calculator",
                          style={"margin-bottom": "0px", 'color': 'grey'}, ),
                  html.H5(
                      "Reach your Goal", style={"margin-top": "0px", 'text-color': 'black'}
                  )])
information = html.Div(
    [
        html.H3(
            "Your Information",
            className="control_label",
            style={'text-align': 'center'}
        ),
        html.Br(),
        html.Label(
            [
                html.Div(["Weight"]),
                dcc.Input(
                    id="input1",
                    placeholder="Weight in kg",
                    type="number",
                    # debounce=True,
                    min=0,
                    max=999,
                    #value=70,
                    className="dcc_control"
                ),
            ]
        ),
        html.Br(),
        html.Label(
            [
                html.Div(["Height"]),
                dcc.Input(
                    id="input2",
                    placeholder="Height in cm",
                    type="number",
                    # debounce=True,
                    min=0,
                    max=999,
                    #value=175
                ),
            ]
        ),
        html.Br(),
        html.Label(
            [
                html.Div(["Age"]),
                dcc.Input(
                    id="input3",
                    placeholder="Age",
                    type="number",
                    # debounce=True,
                    min=0,
                    max=200,
                    #value=22
                ),
            ]
        ),
        html.Br(),
        html.Label(
            [
                html.Div(["Gender"]),
                dcc.RadioItems(
                    id="input4",
                    options=[
                        {'label': 'Male', 'value': 'M'},
                        {'label': 'Female', 'value': 'F'},
                    ],
                    labelStyle={'display': 'inline-block', 'width': '5%', 'padding': '.5em .5em'},
                    #value='F'
                )
            ]
        ),
        html.Br(),
        html.Label(
            [
                html.Div(["Activity"]),
                dcc.Dropdown(
                    id="input5",
                    options=[
                        {'label': 'lightly', 'value': 'l'},
                        {'label': 'moderate', 'value': 'm'},
                        {'label': 'very', 'value': 'v'},
                    ],
                    style={'width': '60%', 'display': 'inline-block'},
                    #value='m'
                )
            ]
        ),
        # Goal
        html.Br(),
        html.H3(
            "Goal Information",
            className="control_label",
            style={'text-align': 'center'}
        ),
        html.Br(),
        html.Label(
            [
                html.Div(["Goal Weight"]),
                dcc.Input(
                    id="input6",
                    placeholder="Goal",
                    type="number",
                    # debounce=True,
                    min=0,
                    max=200,
                    #value=50
                ),
            ]
        ),
        html.Br(),
        html.Label(
            [
                html.Div(["Days to achieve goal"]),
                dcc.Input(
                    id="input7",
                    className='control_label',
                    placeholder="Days",
                    type="number",
                    min=0,
                    max=999,
                    #value=22
                ),
            ]
        ),
        html.Br(),
        dbc.Button(id='buttonSubmit', n_clicks=0, children='Submit', color="primary",
                   style={"backgroundColor": "#BFC1C1", 'color': 'black', 'left': '37%', 'position': 'absolute'}),
        html.Br(),
        html.Br(),
        html.Div(
            id="info-container",
            style={'text-align': 'center'}
        ),
    ],
    className="pretty_container four columns",
    id="information-log-section",
)
dailylog = html.Div([
    html.Div(
        [
            html.Div(
                [
                    html.H4(
                        "Choose Date to Log for:",
                        className="control_label",
                        style={'text-align': 'left'}
                    ),
                    html.Br(),
                    # all dates until goal days end
                    dcc.Dropdown(
                        id="inputdate",
                        options=[
                            {'label': 'today', 'value': 'today'}
                        ],
                        style={'width': '60%', 'display': 'inline-block'}
                    ),
                    dbc.Button(id='buttonLog', n_clicks=0, children='Submit Day Log', color="primary",
                               style={"backgroundColor": "#BFC1C1", 'display': 'inline-block', 'color': 'black'}),
                ],
                id="date",
                className="pretty_container columns",
            )

        ],
        id="date-container",
        className="row container-display",
    ),

    html.Div(
        [
            # Foods Div
            html.Div(
                [
                    html.Label(
                        [

                            html.H4(
                                "Food You Ate",
                                className="control_label",
                                style={'text-align': 'center'}
                            ),
                            html.Br(),
                            dcc.Dropdown(  # import from foods csv
                                id="inputfood",
                                options=food_options,
                                style={'width': '60%', 'display': 'inline-block'}
                            ),
                            dbc.Button(id='buttonfood', n_clicks=0, children='Add', color="primary",
                                       style={"backgroundColor": "#BFC1C1", 'color': 'black'}),
                        ]
                    ),
                    html.Br(),
                    html.Div(
                        children=[
                            dash_table.DataTable(
                                id='food_table',
                                columns=[{'id': 'food', 'name': 'food'}, {'id': 'calories', 'name': 'calories'}],
                                data=(pd.DataFrame(data={'food': [], 'calories': []})).to_dict('records'),
                                fixed_rows={'headers': True},
                                style_data_conditional=[
                                    {
                                        "if": {"column_id": "param"},
                                        "textAlign": "right",
                                        # "paddingRight": 10,
                                    },
                                    {
                                        "if": {"row_index": "even"},
                                        "backgroundColor": "rgb(224, 224, 224)",
                                    },
                                ],
                                style_data={
                                    'overflow': 'hidden',
                                    'textOverflow': 'ellipsis',
                                    'maxWidth': 0
                                },
                            )
                        ],
                        id="foodtable"), ],
                className="pretty_container seven columns",
            ),
            # Workput Div
            html.Div(
                [
                    html.Label(
                        [
                            html.H4(
                                "Calories You Burned",
                                className="control_label",
                                style={'text-align': 'center'}
                            ),
                            html.Br(),
                            dcc.Input(
                                id="inputworkout",
                                className='control_label',
                                type="number",
                                # debounce=True,
                                min=0,
                                value=0
                            ),
                        ]
                    ),
                ],
                className="pretty_container five columns",
            )
        ],
        className="row flex-display",
    ),
    html.Br(),
    html.Div(
        # day table
        [html.Div(
            children=[
                dash_table.DataTable(
                    id='day_table',
                    columns=[{'id': 'date', 'name': 'date'}, {'id': 'calories in', 'name': 'calories in'},
                             {'id': 'calories out', 'name': 'calories out'}],
                    data=(pd.DataFrame(data={'date': [], 'calories in': [], 'calories out': []})).to_dict('records'),
                    fixed_rows={'headers': True},
                    style_data_conditional=[
                        {
                            "if": {"column_id": "param"},
                            "textAlign": "right",
                            # "paddingRight": 10,
                        },
                        {
                            "if": {"row_index": "even"},
                            "backgroundColor": "rgb(224, 224, 224)",
                        },
                    ],
                    style_data={
                        'overflow': 'hidden',
                        'textOverflow': 'ellipsis',
                        'maxWidth': 0
                    },
                )
            ],
            id="daytable",
            className="pretty_container ten columns",
        )
        ],
        className="row flex-display",
    ),
    html.Br(),
    html.Div([
        dbc.Button(id='buttonSummary', n_clicks=0, children='Show Summary', color="primary",
                   style={"backgroundColor": "#BFC1C1", 'color': 'black'}, ),
    ],
        className="row flex-display"
    )
],
    id="day-log-section",
    className="eight columns",
)
ending = html.Div(
    [

        html.Div(
            children=[
                html.Img(src='assets/i3.svg', style={"width": "400px", "height": "200px"}),
                dcc.Markdown("**Submit your information**")
            ],
            style={"text-align": "center"},
            id="img1",
            className="pretty_container"),

        html.Div(
            children=[
                html.Img(src='assets/i1.svg', style={"width": "400px", "height": "200px"}),
                dcc.Markdown("**Set a Goal**")
            ],
            style={"text-align": "center"},
            id="img4",
            className="pretty_container"),

        html.Div(
            children=[
                html.Img(src='assets/i4.svg', style={"width": "400px", "height": "200px"}),
                dcc.Markdown("**Log your daily calories**")
            ],
            style={"text-align": "center"},
            id="img2",
            className="pretty_container  "),
        html.Div(
            children=[
                html.Img(src='assets/i2.svg', style={"width": "400px", "height": "200px"}),
                dcc.Markdown("**See Summary**")

            ],
            style={"text-align": "center"},
            id="img3",
            className="pretty_container  "),

    ],
    className="row flex-display", )
# Create app layout
app.layout = html.Div(
    [
        dcc.Store(id="aggregate_data"),
        # empty Div to trigger javascript file for graph resizing
        html.Div(id="output-clientside"),
        # Top Part
        html.Div(
            [
                html.Div(
                    [],
                    className="one-third column",
                ),
                html.Div(
                    [title],
                    className="one-half column",
                    id="title",
                ),
                html.Div(
                    [
                        html.A(
                            html.Button("Github", id="learn-more-button"),
                            href="https://github.com/linaghanim/CourseWork",
                        )],
                    className="one-third column",
                    id="button",
                ),
            ],
            id="header",
            className="row flex-display",
            style={"margin-bottom": "25px"}, ),
        # Information and Daily Log Part
        html.Div(
            children=[information, dailylog],
            id='appsections',
            className="row flex-display"),
        # Summary Part
        html.Div(
            [
                html.Div(
                    children=[],
                    id="results_table",
                    className="pretty_container seven columns"),

                html.Div(
                    children=[],
                    id='results_graph',
                    className="pretty_container five columns"),
            ],
            className="row flex-display", ),
        html.Br(),
        html.Div([ending]),
    ],
    id="mainContainer",
    style={"display": "flex", "flex-direction": "column"},
)


# Information section
@app.callback(
    [Output('day-log-section', 'style'), Output('info-container', 'children'), Output("inputdate", "options")],
    [Input('buttonSubmit', 'n_clicks')],
    [State("input1", "value"), State("input2", "value"), State("input3", "value"),
     State("input4", "value"), State("input5", "value"), State("input6", "value"),
     State("input7", "value")]
)
def submit_func(n_clicks, weight, height, age, gender, activity, goal, days):
    date_options = [{'label': '', 'value': ''}]
    calories_in.clear()
    calories_out.clear()
    daily_calories.clear()
    if weight == None or height == None or age == None or gender == None or activity == None or goal == None or days == None:
        return [{'display': 'none'}, None, date_options]
    # calculate users' calories
    cal = calories_calc(weight, height, age, gender, activity)
    maintaincal = "Eat " + str(round(cal)) + " Calories/Day to Maintain Weight"
    # create dates list from today until user's goal
    dateoptions = create_dates_list(days)
    return [{'display': 'block'}, maintaincal, dateoptions]


# Food Section
@app.callback(
    Output('food_table', 'data'), Output('inputworkout', 'value'), Output('day_table', 'data'),
    Output('day_table', 'columns'),
    Input('buttonfood', 'n_clicks'), Input('buttonLog', 'n_clicks'),
    State('food_table', 'data'), State('food_table', 'columns'),
    State('day_table', 'data'), State('day_table', 'columns'),
    State("inputfood", "value"), State('inputworkout', 'value'), State("inputdate", "value"))
def add_food(food_clicks, log_clicks, rows1, columns1, rows2, columns2, foodvalue, burnvalue, datevalue):
    changed_id = [p['prop_id'] for p in dash.callback_context.triggered][0]
    if datevalue == None or foodvalue == None:
        return rows1, burnvalue, rows2, []
    columns2 = [{'id': 'date', 'name': 'date'}, {'id': 'calories in', 'name': 'calories in'},
                {'id': 'calories out', 'name': 'calories out'}]
    # Add day calories to days table
    if ('buttonLog' in changed_id):
        calories_out[datevalue] = burnvalue
        daily_calories[datevalue] = [calories_in[datevalue], calories_out[datevalue]]  # save calories
        # create new rows from new daily_calories
        rows2 = (pd.DataFrame(
            data={'date': list(daily_calories.keys()), 'calories in': Extract(list(daily_calories.values()), 0),
                  'calories out': Extract(list(daily_calories.values()), 1)})).to_dict('records')
        return (pd.DataFrame(data={'food': [], 'calories': []})).to_dict('records'), 0, rows2, columns2
    # Add food to food table
    if ('buttonfood' in changed_id):
        rows1.append({'food': foodvalue, 'calories': food[foodvalue]})
        if datevalue in list(daily_calories.keys()):
            calories_in[datevalue] = 0
            del daily_calories[datevalue]
        calories_in[datevalue] = calories_in[datevalue] + int(food[foodvalue])
    return rows1, burnvalue, rows2, columns2


# Summary Section
@app.callback(
    [Output("results_graph", "children"), Output("results_table", "children"), ],
    [Input('buttonSummary', 'n_clicks')],
    [State("input1", "value"), State("input2", "value"), State("input3", "value"),
     State("input4", "value"), State("input5", "value"), State("input6", "value"),
     State("input7", "value"), State("inputdate", "value")]
)
def submit_func(n_clicks, weight, height, age, gender, activity, goal, days, datevalue):
    changed_id = [p['prop_id'] for p in dash.callback_context.triggered][0]
    if ('buttonSummary' not in changed_id):
        return None, None
    # weight summary table
    weight_data = weight_data_func(weight, goal, days)
    if weight_data == None:
        return "Please enter daily foods and workouts to see summary table", ""
    weight_df = pd.DataFrame(data=weight_data)
    table = dash_table.DataTable(
        id='weighttable',
        columns=[{'id': c, 'name': c} for c in weight_df.columns],
        data=weight_df.to_dict('records'),
        fixed_rows={'headers': True},
        style_data_conditional=[
            {
                "if": {"column_id": "param"},
                "textAlign": "right",
            },
            {
                "if": {"row_index": "even"},
                "backgroundColor": "rgb(224, 224, 224)",
            },
        ],
        style_data={
            'overflow': 'hidden',
            'textOverflow': 'ellipsis',
            'maxWidth': 0
        },
    )
    # Linear weight loss
    pred_data = pred_data_func(weight, goal, days)
    pred_df = pd.DataFrame(data=pred_data)

    # real figure
    fig1 = go.Figure()
    fig1.update_layout(
        title="Real Weight")
    fig1.add_trace(go.Scatter(x=weight_df['day'], y=weight_df['weight'], mode='lines', name='real'))
    graph1 = dcc.Graph(id="real_graph", figure=fig1)

    # line figure
    fig2 = go.Figure()
    fig2.update_layout(
        title="Predict Weight")
    fig2.add_trace(go.Scatter(x=pred_df['day'], y=pred_df['weight'], mode='lines', name='linear'))
    graph2 = dcc.Graph(id="pred_graph", figure=fig2)

    return [graph1, graph2], table


# Functions
# create date options
def create_dates_list(days):
    d = date.today()
    for i in range(days):
        # initialize calories-in and calories_out dictionary
        calories_in[str(d)] = 0
        calories_out[str(d)] = 0
        d += timedelta(days=1)
    date_options = [{'label': name, 'value': name} for name in calories_in.keys()]
    return date_options


# calculate maintenance calories
def calories_calc(weight, height, age, gender, activity):
    if gender == 'F':
        bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
    else:
        bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
    if activity == 'l':
        cal = bmr * 1.375
    elif activity == 'm':
        cal = bmr * 1.55
    else:
        cal = bmr * 1.725
    return cal


# linear weight change
def pred_data_func(start, goal, days):
    day = []
    weight = []
    dates = []
    c = 1
    d = date.today()
    lose = (start - goal) / days # lose equal weight everyday
    for w in np.arange(start, goal, -lose):
        # week number
        day.append(c)
        c += 1
        # weight
        weight.append(w)
        # date
        dates.append(str(d))
        d += timedelta(days=1)
    data = {'day': day, 'date': dates, 'weight': weight}
    return data


# actual weight change
def weight_data_func(start, goal, days):
    day = []
    calin = list(calories_in.values())
    calout = list(calories_out.values())
    caldiff = []
    weights = []
    dates = list(calories_in.keys())
    c = 0
    prevweight = weight = start
    if len(calories_out) == 0:
        return None
    while c < len(calories_in):
        # week number
        day.append(c)
        diff = calin[c] - calout[c]
        caldiff.append(diff)
        weight = prevweight + float(diff / 3500.0)  # 1 pound = 3500 calories
        prevweight = weight
        weights.append(weight)
        c += 1
    data = {'day': day, 'date': dates, 'calories in': calin, 'calories out': calout, 'diff': caldiff, 'weight': weights}
    return data

# create list of elements in index i
def Extract(lst, i):
    return [item[i] for item in lst]

if __name__ == "__main__":
    app.run_server(debug=True)
