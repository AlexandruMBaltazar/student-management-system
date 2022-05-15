import {useState, useEffect} from 'react'
import {getAllStudents, deleteStudent} from "./client";
import {
    Layout,
    Menu,
    Breadcrumb,
    Table,
    Spin,
    Empty,
    Button,
    Badge,
    Space,
    Tag,
    Avatar,
    Radio,
    Popconfirm
} from 'antd';
import {
    DesktopOutlined,
    PieChartOutlined,
    FileOutlined,
    TeamOutlined,
    UserOutlined,
    LoadingOutlined, PlusCircleOutlined
} from '@ant-design/icons';
import StudentDrawerForm from "./StudentDrawerForm";
import './App.css';
import {errorNotification, successNotification} from "./Notification";

const {Header, Content, Footer, Sider} = Layout;
const {SubMenu} = Menu;

const TheAvatar = ({name}) => {
    let trim = name.trim();
    if (trim.length === 0) {
        return <Avatar icon={<UserOutlined></UserOutlined>}/>
    }
    const split = trim.split(" ")
    if (split.length === 1) {
        return <Avatar>{name.charAt(0)}</Avatar>
    }
    return <Avatar>{`${name.charAt(0)}${name.charAt(name.length - 1)}`}</Avatar>
}

const removeStudent = (studentId, callback) => {
    deleteStudent(studentId).then(() => {
        successNotification( "Student deleted", `Student with ${studentId} was deleted`);
        callback();
    });
}

const columns = fetchStudents => [
    {
        title: '',
        dataIndex: 'avatar',
        key: 'avatar',
        render: (text, student) => <TheAvatar name={student.name}/>
    },
    {
        title: 'Id',
        dataIndex: 'id',
        key: 'id',
    },
    {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: 'Email',
        dataIndex: 'email',
        key: 'email',
    },
    {
        title: 'Gender',
        dataIndex: 'gender',
        key: 'gender',
    },
    {
        title: 'Actions',
        key: 'actions',
        render: (text, student) =>
            <Radio.Group>
                <Popconfirm
                    placement='topRight'
                    title={`Are you sure to delete ${student.name}`}
                    onConfirm={() => removeStudent(student.id, fetchStudents)}
                    okText='Yes'
                    cancelText='No'>
                    <Radio.Button value="small">Delete</Radio.Button>
                </Popconfirm>
                <Radio.Button value="small">Edit</Radio.Button>
            </Radio.Group>
    },
];

const antIcon = <LoadingOutlined style={{fontSize: 24}} spin/>;

function App() {
    const [students, setStudents] = useState([]);
    const [collapsed, setCollapsed] = useState(false);
    const [fetching, setFetching] = useState(true);
    const [showDrawer, setShowDrawer] = useState(false);

    const fetchStudents = () =>
        getAllStudents()
            .then(res => res.json())
            .then(data => {
                console.log(data);
                setStudents(data);
                setFetching(false);
            })
            .catch(error => {
                console.log(error.response)
                error.response.json().then(res => {
                    console.log(res)
                    errorNotification(
                        "There was an issue",
                        res.message
                    )
                })
            })
            .finally(() => setFetching(false))

    useEffect(() => {
        console.log("component is mounted");
        fetchStudents();
    }, []);

    const renderStudents = () => {
        if (fetching) {
            return <Spin indicator={antIcon}/>
        }
        if (students.length <= 0) {
            return <Empty/>;
        }
        return <>
            <StudentDrawerForm showDrawer={showDrawer} setShowDrawer={setShowDrawer}/>
            <Table
                dataSource={students}
                columns={columns(fetchStudents)}
                bordered
                title={() =>
                    <>
                        <Tag>Number of students</Tag>
                        <Badge style={{backgroundColor: '#d9d9d9'}}
                               count={students.length}/>
                        <br/><br/>
                        <Button onClick={() => setShowDrawer(!showDrawer)} type="primary" shape="round"
                                icon={<PlusCircleOutlined/>} size="medium">
                            Add New Student
                        </Button>
                    </>
                }
                pagination={{pageSize: 20}}
                scroll={{y: 500}}
            />
        </>;
    }

    return <Layout style={{minHeight: '100vh'}}>
        <Sider collapsible collapsed={collapsed}
               onCollapse={setCollapsed}>
            <div className="logo"/>
            <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
                <Menu.Item key="1" icon={<PieChartOutlined/>}>
                    Option 1
                </Menu.Item>
                <Menu.Item key="2" icon={<DesktopOutlined/>}>
                    Option 2
                </Menu.Item>
                <SubMenu key="sub1" icon={<UserOutlined/>} title="User">
                    <Menu.Item key="3">Tom</Menu.Item>
                    <Menu.Item key="4">Bill</Menu.Item>
                    <Menu.Item key="5">Alex</Menu.Item>
                </SubMenu>
                <SubMenu key="sub2" icon={<TeamOutlined/>} title="Team">
                    <Menu.Item key="6">Team 1</Menu.Item>
                    <Menu.Item key="8">Team 2</Menu.Item>
                </SubMenu>
                <Menu.Item key="9" icon={<FileOutlined/>}>
                    Files
                </Menu.Item>
            </Menu>
        </Sider>
        <Layout className="site-layout">
            <Header className="site-layout-background" style={{padding: 0}}/>
            <Content style={{margin: '0 16px'}}>
                <Breadcrumb style={{margin: '16px 0'}}>
                    <Breadcrumb.Item>User</Breadcrumb.Item>
                    <Breadcrumb.Item>Bill</Breadcrumb.Item>
                </Breadcrumb>
                <div className="site-layout-background" style={{padding: 24, minHeight: 360}}>
                    {renderStudents()}
                </div>
            </Content>
            <Footer style={{textAlign: 'center'}}>By Alex</Footer>
        </Layout>
    </Layout>
}

export default App;